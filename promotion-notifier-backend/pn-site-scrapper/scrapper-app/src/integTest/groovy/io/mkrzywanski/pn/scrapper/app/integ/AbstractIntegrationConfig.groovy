package io.mkrzywanski.pn.scrapper.app.integ;

import com.mongodb.ConnectionString
import io.mkrzywanski.pn.scrapper.app.infra.MongoDbTransactionConfig
import io.mkrzywanski.pn.testcontainers.ContainerCommandWaitStrategy
import org.awaitility.Awaitility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Configuration
@EnableAutoConfiguration
@Import(MongoDbTransactionConfig)
abstract class AbstractIntegrationConfig {

    @Autowired
    protected Environment environment

    @Bean
    GenericContainer<?> mongoDBContainer() {
        def database = environment.getProperty("spring.data.mongodb.database")
        def username = environment.getProperty("spring.data.mongodb.username")
        def password = environment.getProperty("spring.data.mongodb.password")

        def mongoReplicaSetReady = ContainerCommandWaitStrategy.builder()
                .command("mongo", "--quiet", "--port", "27017", "-u", "root", "-p", "password", "--eval", "rs.status().ok")
                .expectedOutput("1\n")
                .build()
        def mongoDBContainer = new GenericContainer<>("bitnami/mongodb:4.4.12")
                .withEnv("MONGODB_USERNAME", username)
                .withEnv("MONGODB_PASSWORD", password)
                .withEnv("MONGODB_DATABASE", database)
                .withEnv("MONGODB_REPLICA_SET_MODE", "primary")
                .withEnv("MONGODB_REPLICA_SET_KEY", "someKey1")
                .withEnv("MONGODB_ROOT_PASSWORD", "password")
                .waitingFor(mongoReplicaSetReady)
                .withExposedPorts(27017)
        mongoDBContainer.start()
        mongoDBContainer
    }

    @Bean
    MongoClientSettingsBuilderCustomizer mongoSettingsCustomizer(final GenericContainer<?> mongoDBContainer) {
        def database = environment.getProperty("spring.data.mongodb.database")
        def connectionString = new ConnectionString("mongodb://localhost:${mongoDBContainer.firstMappedPort}/${database}?replicaSet=replicaset")
        return (settings) -> settings.applyConnectionString(connectionString)
    }
}
