package io.mkrzywanski;

import io.mkrzywanski.pn.matching.infa.QueueConfig;
import io.mkrzywanski.pn.matching.matchedoffers.Offer;
import io.mkrzywanski.pn.matching.matchedoffers.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestConfig.class, QueueConfig.class})
@AutoConfigureStubRunner(ids = "io.mkrzywanski:scrapper-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@Testcontainers
@ActiveProfiles("test")
public class PostConsumingContractTest {

    private static final String RABBIT_USERNAME = "test";
    private static final String RABBIT_PASSWORD = "test";
    private static final String RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18";
    private static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq");

    @Container
    private static RabbitMQContainer rabbit = new RabbitMQContainer(RABBIT_IMAGE)
            .withEnv("RABBITMQ_USERNAME", RABBIT_USERNAME)
            .withEnv("RABBITMQ_PASSWORD", RABBIT_PASSWORD);

    @Autowired
    private StubTrigger trigger;

    @Autowired
    private PostConsumer postConsumer;

    @DynamicPropertySource
    private static void rabbitProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", () -> RABBIT_USERNAME);
        registry.add("spring.rabbitmq.password", () -> RABBIT_PASSWORD);
    }

    @Test
    public void contextLoads() {
        this.trigger.trigger("trigger");

        await().untilAsserted(() -> then(this.postConsumer.size()).isEqualTo(1));

        final List<Post> posts = postConsumer.messages();
        final Post post = posts.stream().findFirst().orElseThrow(IllegalStateException::new);

        assertThat(post.getId()).isNotNull();
        assertThat(post.getOffers()).contains(new Offer("Rainbow Six", Map.of(Currency.getInstance("PLN"), BigDecimal.ONE), "www.test.pl"));

    }
}

@Configuration
@EnableAutoConfiguration
class TestConfig {

    @Bean
    PostConsumer postConsumer() {
        return new PostConsumer();
    }

    @Bean
    MessageVerifier<Message> testMessageVerifier(final RabbitTemplate rabbitTemplate) {
        return new SimpleMessageVerifier(rabbitTemplate);
    }
}

class PostConsumer {

    private final BlockingQueue<Post> queue = new LinkedBlockingQueue<>();

    @RabbitListener(queues = "${gpn.queue.name}")
    void consume(final Post json) {
        queue.add(json);
    }

    int size() {
        return queue.size();
    }

    List<Post> messages() {
        return queue.stream().toList();
    }
}
