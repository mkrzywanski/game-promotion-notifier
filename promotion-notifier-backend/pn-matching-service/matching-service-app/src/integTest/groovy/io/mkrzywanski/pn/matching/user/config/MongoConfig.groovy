package io.mkrzywanski.pn.matching.user.config

import com.mongodb.ConnectionString
import org.awaitility.Awaitility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.testcontainers.containers.Container
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import java.time.Duration
import java.util.concurrent.TimeUnit

class MongoConfig {

    @Autowired
    protected Environment environment

    @Bean
    GenericContainer<?> mongoDBContainer() {
        def database = environment.getProperty("spring.data.mongodb.database")
        def username = environment.getProperty("spring.data.mongodb.username")
        def password = environment.getProperty("spring.data.mongodb.password")

        def mongoDBContainer = new GenericContainer<>("bitnami/mongodb:4.4.12")
                .withEnv("MONGODB_USERNAME", username)
                .withEnv("MONGODB_PASSWORD", password)
                .withEnv("MONGODB_DATABASE", database)
                .withEnv("MONGODB_REPLICA_SET_MODE", "primary")
                .withEnv("MONGODB_REPLICA_SET_KEY", "someKey1")
                .withEnv("MONGODB_ROOT_PASSWORD", "password")
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(10)))
                .withExposedPorts(27017)
        mongoDBContainer.start()
        Awaitility.await().until(() -> {
            def result = mongoDBContainer.execInContainer("mongo", "--quiet", "--port", "27017",  "-u" , "root", "-p",  "password", "--eval", "rs.status().ok")
            result.stdout == "1\n"
        })

        mongoDBContainer
    }

    @Bean
    MongoClientSettingsBuilderCustomizer mongoSettingsCustomizer(final GenericContainer<?> mongoDBContainer) {
        def database = environment.getProperty("spring.data.mongodb.database")
        def connectionString = new ConnectionString("mongodb://localhost:${mongoDBContainer.firstMappedPort}/${database}?replicaSet=replicaset")
        return (settings) -> settings.applyConnectionString(connectionString)
    }
}
