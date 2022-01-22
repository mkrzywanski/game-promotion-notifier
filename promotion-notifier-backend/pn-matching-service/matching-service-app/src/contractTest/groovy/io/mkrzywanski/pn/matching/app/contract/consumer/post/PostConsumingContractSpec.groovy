package io.mkrzywanski.pn.matching.app.contract.consumer.post

import io.mkrzywanski.pn.matching.infra.queue.PostsQueueConfig
import io.mkrzywanski.pn.matching.infra.queue.RabbitConfig
import io.mkrzywanski.pn.matching.matchedoffers.Offer
import io.mkrzywanski.pn.matching.matchedoffers.Post
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
import spock.lang.Specification

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.BDDAssertions.then
import static org.awaitility.Awaitility.await

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [TestConfig, RabbitConfig, PostsQueueConfig])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:scrapper-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
class PostConsumingContractSpec extends Specification {

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
    private PostConsumer postConsumer

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
        await().untilAsserted(() -> then(this.postConsumer.size()).isEqualTo(1))

        when:
        final List<Post> posts = postConsumer.messages()
        final Post post = posts.stream().findFirst().orElseThrow(IllegalStateException.&new)

        then:
        assertThat(post.getId()).isNotNull()
        assertThat(post.getOffers()).contains(new Offer(UUID.fromString("856e68ac-2ae9-4164-bbda-374663b91cdd"), "Rainbow Six", Map.of(Currency.getInstance("PLN"), BigDecimal.ONE), "www.test.pl"))

    }

//    void cleanup() {
//        RABBIT_MQ_CONTAINER.stop()
//    }
}

@Configuration
@EnableAutoConfiguration
class TestConfig {

    @Bean
    PostConsumer postConsumer() {
        return new PostConsumer()
    }

    @Bean
    MessageVerifier<Message> testMessageVerifier(final RabbitTemplate rabbitTemplate) {
        return new SimpleMessageVerifier(rabbitTemplate)
    }
}

class PostConsumer {

    private final BlockingQueue<Post> queue = new LinkedBlockingQueue<>()

    @RabbitListener(queues = '${gpn.queue.name}')
    void consume(final Post post) {
        queue.add(post)
    }

    int size() {
        return queue.size()
    }

    List<Post> messages() {
        return queue.stream().toList()
    }
}
