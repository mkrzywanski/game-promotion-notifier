package io.mkrzywanski.pn.matching.matchedoffers

import com.github.tomakehurst.wiremock.WireMockServer
import io.mkrzywanski.pn.matching.MatchingServiceApp
import io.mkrzywanski.pn.matching.keycloak.KeyCloakAccess
import io.mkrzywanski.pn.matching.keycloak.KeyCloakContainer
import io.mkrzywanski.pn.matching.keycloak.KeyCloakProperties
import io.mkrzywanski.pn.matching.keycloak.KeycloakClient
import io.mkrzywanski.pn.matching.keycloak.KeycloakUser
import io.mkrzywanski.pn.matching.user.config.MongoConfig
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.stereotype.Component
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static org.assertj.core.api.Assertions.*
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = [MongoConfig, TestConfig, TestNotificationConsumer, MatchingServiceApp])
@AutoConfigureWireMock(port = 0)
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
    MongoMatchesRepository matchesRepository

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
class TestConfig {
    @Bean
    KeyCloakContainer keyCloakContainer(final KeyCloakProperties keyCloakProperties) {
        KeyCloakContainer keyCloakContainer = new KeyCloakContainer(keyCloakProperties.adminUser)
        keyCloakContainer.start()
        setupKeycloak(keyCloakProperties, keyCloakContainer.getFirstMappedPort())
        keyCloakContainer
    }

    @Bean
    KeyCloakProperties keyCloakProperties() {
        def client = new KeycloakClient("pn-matching-service", "secret")
        def user = new KeycloakUser("test", "test")
        def admin = new KeycloakUser("admin", "admin")
        new KeyCloakProperties(client, KeyCloakProperties.ADMIN_CLI_CLIENT, user, admin, "xD")
    }

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

    @Bean
    KeyCloakAccess keycloak(KeyCloakProperties keyCloakProperties, KeyCloakContainer keyCloakContainer) {
        def adminAccess = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${keyCloakContainer.getFirstMappedPort()}/auth")
                .realm("master")
                .clientId(keyCloakProperties.adminCliClient.clientId)
                .username(keyCloakProperties.adminUser.username)
                .password(keyCloakProperties.adminUser.password)
                .build()
        def userAccess = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${keyCloakContainer.getFirstMappedPort()}/auth")
                .realm(keyCloakProperties.testRealm)
                .clientId(keyCloakProperties.client.clientId)
                .clientSecret(keyCloakProperties.client.clientSecret)
                .username(keyCloakProperties.user.username)
                .password(keyCloakProperties.user.password)
                .build()
        new KeyCloakAccess(adminAccess, userAccess)
    }

    def setupKeycloak(KeyCloakProperties keyCloakProperties,
                      int port) {
        def keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:${port}/auth")
                .realm("master")
                .clientId(keyCloakProperties.adminCliClient.clientId)
                .username(keyCloakProperties.adminUser.username)
                .password(keyCloakProperties.adminUser.password)
                .build()

        def realm = testRealm(keyCloakProperties.testRealm)
        keycloak.realms().create(realm)

        def clientRepresentation = testClient(keyCloakProperties.client)
        keycloak.realm(keyCloakProperties.testRealm).clients().create(clientRepresentation)

        def user = testUser(keyCloakProperties.user)
        keycloak.realm(keyCloakProperties.testRealm).users().create(user)

    }

    private UserRepresentation testUser(KeycloakUser keycloakUser) {
        CredentialRepresentation credential = new CredentialRepresentation()
        credential.type = CredentialRepresentation.PASSWORD
        credential.value = keycloakUser.password

        UserRepresentation user = new UserRepresentation()
        user.username = keycloakUser.username
        user.firstName = "test"
        user.lastName = "test"
        user.email = "test@gmail.com"
        user.credentials = Arrays.asList(credential)
        user.enabled = true
        user.realmRoles = Arrays.asList("admin")
        user
    }

    private def testRealm(String realm) {
        def rep = new RealmRepresentation()
        rep.realm = realm
        rep.enabled = true
        rep
    }

    private def testClient(KeycloakClient keycloakClient) {
        def clientRepresentation = new ClientRepresentation()
        clientRepresentation.clientId = keycloakClient.clientId
        clientRepresentation.secret = keycloakClient.clientSecret
        clientRepresentation.redirectUris = Arrays.asList("*")
        clientRepresentation.directAccessGrantsEnabled = true
        clientRepresentation.standardFlowEnabled = true
        clientRepresentation.serviceAccountsEnabled = true
        clientRepresentation
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
