package io.mkrzywanski.pn.matching.matchedoffers

import com.github.tomakehurst.wiremock.WireMockServer
import io.mkrzywanski.pn.matching.MatchingServiceApp

import io.mkrzywanski.pn.matching.user.config.MongoConfig
import io.mkrzywanski.shared.keycloak.KeyCloakContainer
import io.mkrzywanski.shared.keycloak.KeyCloakProperties
import io.mkrzywanski.shared.keycloak.spring.KeycloakContainerConfiguration
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static org.assertj.core.api.Assertions.assertThat
import static org.awaitility.Awaitility.await

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ContextConfiguration(classes = [MongoConfig, TestConfig, TestNotificationConsumer, MatchingServiceApp])
class PostsConsumerToNotificationProductionSpec extends Specification {

    private static final String RABBIT_USERNAME = "test"
    private static final String RABBIT_PASSWORD = "test"
    private static final String RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18"
    private static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq")

    @Shared
    private static RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT_IMAGE)
            .withEnv("RABBITMQ_USERNAME", RABBIT_USERNAME)
            .withEnv("RABBITMQ_PASSWORD", RABBIT_PASSWORD)

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        RABBIT_MQ_CONTAINER.start()
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort)
        registry.add("spring.rabbitmq.username", { -> RABBIT_USERNAME })
        registry.add("spring.rabbitmq.password", { -> RABBIT_PASSWORD })
    }

    @Value('${gpn.queue.name}')
    private String newPostsQueue

    @Autowired
    RabbitTemplate rabbitTemplate

    @Autowired
    TestNotificationConsumer testNotificationConsumer

    @Autowired
    WireMockServer wireMockServer

    def postId = UUID.fromString("cb53e434-0dc0-4f66-b796-7b7beadbad3d")
    def offerId = UUID.fromString("b0e05d9c-fdfe-458e-9aaa-2a251e026205")
    def userId = UUID.fromString("8e3a885f-0708-4b84-903f-e383ebc2ee40")
    def offerName = "offerName"
    def postLink = "link"
    def offers = List.of(new Offer(offerId, offerName, Map.of(Currency.getInstance("PLN"), BigDecimal.ONE), "link"))
    def post = new Post(postId, postLink, offers)

    void setup() {
        def subscriptionMatchingRequestBody = """
        {
            "postsToMatch" : [
                {
                    "postId" : "${postId}",
                    "offers" : [
                        {
                            "id" : "${offerId}",
                            "text" : "${offerName}"
                        }
                    ]
                }
            ]
        }
        """.trim()
        wireMockServer.addStubMapping(post(urlEqualTo("/v1/subscriptions/match")).withRequestBody(equalToJson(subscriptionMatchingRequestBody)).withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                            "matches" : [
                                {
                                    "userId" : "${userId}",
                                    "postId" : "${postId}",
                                    "offerId" : "${offerId}"
                                }
                            ]
                        }
                    """)).build())

        wireMockServer.addStubMapping(
                get(urlEqualTo("/v1/users/${userId}"))
                        .withHeader("Content-Type", equalTo("application/json"))
                        .willReturn(aResponse().withBody("""

                        {
                            "userId" : "${userId}",
                            "username" : "test",
                            "firstName" : "test",
                            "email" : "test@test.pl"
                        }
         """).withHeader("Content-Type", "application/json")).build())
    }

    def 'should publish notifications when new post arrives'() {
        when:
        newPostAppearsOnQueue()

        then:
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted({
                    assertThat(testNotificationConsumer.notifications).hasSize(1)
                })
    }

    private newPostAppearsOnQueue() {
        rabbitTemplate.convertAndSend(newPostsQueue, post)
    }
}

@Configuration
@Import(KeycloakContainerConfiguration)
class TestConfig {
    @Bean
    ClientRegistrationRepository clientRegistrationRepository(KeyCloakProperties keyCloakProperties, KeyCloakContainer keyCloakContainer) {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("pn-matching-service")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientId(keyCloakProperties.client.clientId)
                .clientSecret(keyCloakProperties.client.clientSecret)
                .tokenUri("http://localhost:${keyCloakContainer.firstMappedPort}/auth/realms/${keyCloakProperties.testRealm}/protocol/openid-connect/token")
                .build()
        new InMemoryClientRegistrationRepository(clientRegistration)
    }
}

@Component
class TestNotificationConsumer {

    final List<NewOffersNotification> notifications = new CopyOnWriteArrayList<>()

    @RabbitListener(queues = '${gpn.matching-service.publishing.queue.name}')
    void capture(NewOffersNotification notification) {
        notifications.add(notification)
    }

}
