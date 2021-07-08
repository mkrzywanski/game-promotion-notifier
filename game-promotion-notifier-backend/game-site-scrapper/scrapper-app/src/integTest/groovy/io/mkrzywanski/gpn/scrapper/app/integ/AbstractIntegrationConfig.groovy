package io.mkrzywanski.gpn.scrapper.app.integ;

import com.mongodb.ConnectionString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import java.time.Duration

@Configuration
@EnableAutoConfiguration
abstract class AbstractIntegrationConfig {

    @Autowired
    protected Environment environment

    @Bean
    GenericContainer<?> mongoDBContainer() {
        def database = environment.getProperty("spring.data.mongodb.database")
        def username = environment.getProperty("spring.data.mongodb.username")
        def password = environment.getProperty("spring.data.mongodb.password")

        def mongoDBContainer = new GenericContainer<>("mongo:4.4")
                .withEnv("MONGO_INITDB_ROOT_USERNAME", username)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", password)
                .withEnv("MONGO_INITDB_DATABASE", database)
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(10)))
                .withExposedPorts(27017)
        mongoDBContainer.start()
        mongoDBContainer
    }

    @Bean
    MongoClientSettingsBuilderCustomizer mongoSettingsCustomizer(final GenericContainer<?> mongoDBContainer) {
        def database = environment.getProperty("spring.data.mongodb.database")
        def connectionString = new ConnectionString(String.format("mongodb://localhost:%s/%s", mongoDBContainer.getFirstMappedPort(), database))
        return (settings) -> settings.applyConnectionString(connectionString)
    }
}
