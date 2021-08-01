package io.mkrzywanski.pn.scrapper.app.domain.adapters.contract;

import io.mkrzywanski.gpn.scrapper.domain.post.Currencies;
import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.NumberGamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import io.mkrzywanski.pn.scrapper.app.adapters.publishing.QueuePostPublisher;
import io.mkrzywanski.pn.scrapper.app.infra.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {QueueConfig.class, TestConfig.class}, properties = "stubrunner.amqp.mockConnection=false")
@Testcontainers
@AutoConfigureMessageVerifier
public class BaseClass {

    private static final String RABBIT_USERNAME = "test";
    private static final String RABBIT_PASSWORD = "test";
    private static final String RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18";
    private static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq");

    @Container
    private static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT_IMAGE)
            .withEnv("RABBITMQ_USERNAME", RABBIT_USERNAME)
            .withEnv("RABBITMQ_PASSWORD", RABBIT_PASSWORD);

    @Autowired
    private QueuePostPublisher queuePostPublisher;

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", () -> RABBIT_USERNAME);
        registry.add("spring.rabbitmq.password", () -> RABBIT_PASSWORD);
    }

    public void trigger() {
        final List<GameOffer> offers = List.of(new GameOffer("Rainbow Six", new NumberGamePrice(Currencies.PLN, BigDecimal.ONE), "www.test.pl"));
        final Post post = new Post(PostId.generate(), Hash.compute("value"), "value", offers, ZonedDateTime.now());
        final List<Post> posts = List.of(post);
        queuePostPublisher.publish(posts);
    }

}

@Configuration
@Import(QueuePostPublisher.class)
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
class TestConfig {

    @Bean
    RabbitMessageVerifier rabbitTemplateMessageVerifier() {
        return new RabbitMessageVerifier();
    }

    @Bean
    ContractVerifierMessaging<Message<?>> rabbitContractVerifierMessaging(final RabbitMessageVerifier messageVerifier) {
        return new ContractVerifierMessaging<>(messageVerifier) {
            @Override
            protected ContractVerifierMessage convert(final Message message) {
                if (message == null) {
                    return null;
                }
                return new ContractVerifierMessage(message.getPayload(), message.getHeaders());
            }

        };
    }
}

@Slf4j
class RabbitMessageVerifier implements MessageVerifier<Message<?>> {

    private final BlockingQueue<Message<?>> queue = new LinkedBlockingQueue<>();

    @RabbitListener(queues = "${gpn.queue.name}")
    public void listen(final Message<?> message) {
        log.info("Got a message! [{}]", message);
        queue.add(message);
    }

    @Override
    public Message<?> receive(final String destination, final long timeout, final TimeUnit timeUnit, final YamlContract contract) {
        try {
            return queue.poll(timeout, timeUnit);
        } catch (final InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Message<?> receive(final String destination, final YamlContract contract) {
        return receive(destination, 1, TimeUnit.SECONDS, contract);
    }

    @Override
    public void send(final Message message, final String destination, final YamlContract contract) {

    }

    @Override
    public void send(final Object payload, final Map headers, final String destination, final YamlContract contract) {

    }
}
