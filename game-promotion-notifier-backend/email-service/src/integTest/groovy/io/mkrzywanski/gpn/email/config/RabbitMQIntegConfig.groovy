package io.mkrzywanski.gpn.email.config

import io.mkrzywanski.gpn.email.infra.RabbitInfrastructureProperties
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@EnableConfigurationProperties(RabbitInfrastructureProperties)
class RabbitMQIntegConfig {

    private static final RABBIT_MQ_IMAGE = "bitnami/rabbitmq:3.8.18"
    public static final DockerImageName RABBIT_IMAGE = DockerImageName.parse(RABBIT_MQ_IMAGE)
            .asCompatibleSubstituteFor("rabbitmq")

    @Autowired
    Environment environment

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
}
