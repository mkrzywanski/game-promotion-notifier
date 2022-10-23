package io.mkrzywanski.pn.matching.app.contract.producer.notification

import io.mkrzywanski.pn.matching.infra.queue.NotificationsQueueConfig
import io.mkrzywanski.pn.matching.infra.queue.RabbitConfig
import io.mkrzywanski.pn.matching.matchedoffers.NewOffersNotification
import io.mkrzywanski.pn.matching.matchedoffers.OfferNotificationData
import io.mkrzywanski.pn.matching.matchedoffers.PostNotificationData
import io.mkrzywanski.pn.matching.matchedoffers.RabbitNotificationPublisher
import io.mkrzywanski.pn.matching.user.api.UserDetails
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.converter.YamlContract
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.messaging.Message
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [
        TestConfig
], properties = "stubrunner.amqp.mockConnection=false")
@AutoConfigureMessageVerifier
abstract class BaseClass extends Specification {

    private static final String RABBIT_USERNAME = "test"
    private static final String RABBIT_PASSWORD = "test"
    private static final String RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18"
    private static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq")

    @Shared
    static RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT_IMAGE)
            .withEnv("RABBITMQ_USERNAME", RABBIT_USERNAME)
            .withEnv("RABBITMQ_PASSWORD", RABBIT_PASSWORD)

    @Autowired
    private RabbitNotificationPublisher publisher

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        RABBIT_MQ_CONTAINER.start()
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort)
        registry.add("spring.rabbitmq.username", () -> RABBIT_USERNAME)
        registry.add("spring.rabbitmq.password", () -> RABBIT_PASSWORD)
    }

    void trigger() {
        def userId = UUID.fromString("5a1a353b-ffc1-4346-827a-83de9bacd800")
        def details = new UserDetails(userId, "Andrew123", "Andrew", "andrew.golota@gmail.com")
        def postNotifications = List.of(new PostNotificationData("http://test.link", List.of(new OfferNotificationData("Rainbow Six", "http://test.link", Map.of(Currency.getInstance("PLN"), BigDecimal.ONE)))))
        def newOffersNotification = new NewOffersNotification(details, postNotifications)
        publisher.publish(newOffersNotification)
    }

//    void cleanup() {
//        RABBIT_MQ_CONTAINER.stop()
//    }

}

@Configuration
@Import([NotificationsQueueConfig,
        RabbitConfig,
        RabbitNotificationPublisher])
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
class TestConfig {

    @Bean
    RabbitMessageVerifier rabbitTemplateMessageVerifier() {
        return new RabbitMessageVerifier()
    }

    @Bean
    ContractVerifierMessaging<Message<?>> rabbitContractVerifierMessaging(final RabbitMessageVerifier messageVerifier) {
        new ContractVerifierMessaging<Message<?>>(messageVerifier) {
            @Override
            protected ContractVerifierMessage convert(final Message<?> message) {
                if (message == null) {
                    null
                }
                new ContractVerifierMessage(message.getPayload(), message.getHeaders())
            }

        }
    }
}

class RabbitMessageVerifier implements MessageVerifier<Message<?>> {

    private final BlockingQueue<Message<?>> queue = new LinkedBlockingQueue<>()

    @RabbitListener(queues = '${gpn.matching-service.publishing.queue.name}')
    void listen(final Message<?> message) {
        queue.add(message)
    }

    @Override
    Message<?> receive(final String destination, final long timeout, final TimeUnit timeUnit, final YamlContract contract) {
        try {
            return queue.poll(timeout, timeUnit)
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt()
            throw new IllegalStateException(e)
        }
    }

    @Override
    Message<?> receive(final String destination, final YamlContract contract) {
        return receive(destination, 1, TimeUnit.SECONDS, contract)
    }

    @Override
    void send(final Message message, final String destination, final YamlContract contract) {

    }

    @Override
    void send(final Object payload, final Map headers, final String destination, final YamlContract contract) {

    }
}
