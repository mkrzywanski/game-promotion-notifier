package io.mkrzywanski.pn.scrapper.contract

import io.mkrzywanski.gpn.scrapper.domain.post.*
import io.mkrzywanski.pn.scrapper.app.adapters.publishing.QueuePostPublisher
import io.mkrzywanski.pn.scrapper.app.infra.QueueConfig
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

import java.time.ZonedDateTime
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [
        QueueConfig.class,
        TestConfig.class
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
    private QueuePostPublisher queuePostPublisher

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        RABBIT_MQ_CONTAINER.start()
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort)
        registry.add("spring.rabbitmq.username", () -> RABBIT_USERNAME)
        registry.add("spring.rabbitmq.password", () -> RABBIT_PASSWORD)
    }

    void trigger() {
        def gameOffer = new GameOffer(UUID.fromString("856e68ac-2ae9-4164-bbda-374663b91cdd"), "Rainbow Six", new NumberGamePrice(Currencies.PLN, BigDecimal.ONE), "www.test.pl")
        final var offers = List.of(gameOffer)
        final var post = new Post(PostId.generate(), Hash.compute("value"), "value", offers, ZonedDateTime.now())
        final var posts = List.of(post)
        queuePostPublisher.publish(posts)
    }

    void cleanup() {
        RABBIT_MQ_CONTAINER.stop()
    }

}

@Configuration
@Import(QueuePostPublisher.class)
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

    @RabbitListener(queues = '${gpn.queue.name}')
    void listen(final Message<?> message) {
        queue.add(message)
    }

    @Override
    Message<?> receive(final String destination, final long timeout, final TimeUnit timeUnit, final YamlContract contract) {
        try {
            return queue.poll(timeout, timeUnit)
        } catch (final InterruptedException e) {
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
