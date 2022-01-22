package io.mkrzywanski.pn.email.contract

import io.mkrzywanski.pn.email.EmailServiceApplication
import io.mkrzywanski.pn.email.api.NewOffersNotificationData
import io.mkrzywanski.pn.email.api.OfferData
import io.mkrzywanski.pn.email.api.PostData
import io.mkrzywanski.pn.email.api.UserData
import io.mkrzywanski.pn.email.domain.Price
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.StubTrigger
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.BDDAssertions.then
import static org.awaitility.Awaitility.await

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [EmailServiceApplication, TestConfig])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:matching-service-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
class NotificationRequestConsumingTest {

    private static final String RABBIT_USERNAME = "test"
    private static final String RABBIT_PASSWORD = "test"
    private static final String RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18"
    private static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq")

    @Shared
    private static RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT_IMAGE)
            .withEnv("RABBITMQ_USERNAME", RABBIT_USERNAME)
            .withEnv("RABBITMQ_PASSWORD", RABBIT_PASSWORD)
            .withReuse(true)

    @Autowired
    private StubTrigger trigger

    @Autowired
    private NotificationDataConsumer notificationRequestConsumer

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        RABBIT_MQ_CONTAINER.start()
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort)
        registry.add("spring.rabbitmq.username", { -> RABBIT_USERNAME })
        registry.add("spring.rabbitmq.password", { -> RABBIT_PASSWORD })
    }

    void "should consume messages"() {
        given:
        this.trigger.trigger("trigger")
        await().untilAsserted(() -> then(this.notificationRequestConsumer.size()).isEqualTo(1))

        when:
        final def notificationRequests = notificationRequestConsumer.messages()
        final def newOffersNotificationData = notificationRequests.first()

        def userData = new UserData(UUID.fromString("5a1a353b-ffc1-4346-827a-83de9bacd800"), "Andrew123", "Andrew", "andrew.golota@gmail.com")
        def postsData = List.of(new PostData("http://test.link", List.of(new OfferData("Rainbow Six", "http://test.link", Set.of(Price.of(Currency.getInstance("PLN"), BigDecimal.ONE))))))
        final def expected = new NewOffersNotificationData(userData, postsData)

        then:
        assertThat(newOffersNotificationData).isEqualTo(expected)

    }

}

@Configuration
@EnableAutoConfiguration
class TestConfig {

    @Bean
    NotificationDataConsumer norificationDataConsumer() {
        return new NotificationDataConsumer()
    }

    @Bean
    MessageVerifier<Message> testMessageVerifier(final RabbitTemplate rabbitTemplate) {
        new SimpleMessageVerifier(rabbitTemplate)
    }
}

class NotificationDataConsumer {

    private final BlockingQueue<NewOffersNotificationData> queue = new LinkedBlockingQueue<>()

    @RabbitListener(queues = '${gpn.queue.name}')
    void consume(final NewOffersNotificationData post) {
        queue.add(post)
    }

    int size() {
        return queue.size()
    }

    List<NewOffersNotificationData> messages() {
        return queue.stream().toList()
    }
}