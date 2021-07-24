package io.mkrzywanski.gpn.scrapper.app.integ.publishing


import io.mkrzywanski.gpn.scrapper.app.infra.JacksonConfig
import io.mkrzywanski.gpn.scrapper.app.infra.QueueConfig
import io.mkrzywanski.gpn.scrapper.app.integ.AbstractIntegrationConfig
import io.mkrzywanski.gpn.scrapper.app.integ.publishing.NewPostConsumer
import io.mkrzywanski.gpn.scrapper.domain.post.NewPostPublishing
import io.mkrzywanski.gpn.scrapper.domain.post.PostPublisher
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@ComponentScan(["io.mkrzywanski.gpn.scrapper.app.adapters.publishing", "io.mkrzywanski.gpn.scrapper.app.adapters.persistance"])
@Import([NewPostConsumer, JacksonConfig, QueueConfig])
class NewPostPublishingITConfig extends AbstractIntegrationConfig {

    private static final RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18"
    public static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq")

    @Bean(destroyMethod = "")
    RabbitMQContainer rabbitMQContainer() {
        def username = environment.getProperty("spring.rabbitmq.username")
        def password = environment.getProperty("spring.rabbitmq.password")

        RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(RABBIT_IMAGE)
                .withEnv("RABBITMQ_USERNAME", username)
                .withEnv("RABBITMQ_PASSWORD", password)
                .withReuse(true)
        rabbitMQContainer.start()
        rabbitMQContainer
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        def username = environment.getProperty("spring.rabbitmq.username")
        def password = environment.getProperty("spring.rabbitmq.password")

        def factory = new CachingConnectionFactory("localhost", rabbitMQContainer().firstMappedPort)

        factory.setUsername(username)
        factory.setPassword(password)
        factory

    }

    @Bean
    NewPostPublishing newPostPublishing(final PostTransactionalOutboxRepository postTransactionalOutboxRepository,
                                        final PostRepository postRepository,
                                        final PostPublisher postPublisher) {
        return new NewPostPublishing(postTransactionalOutboxRepository, postRepository, postPublisher);
    }

}
