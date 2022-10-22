package io.mkrzywanski.pn.matching.user.config

import com.mongodb.ConnectionString
import io.mkrzywanski.pn.testcontainers.ContainerCommandWaitStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.testcontainers.containers.GenericContainer

class MongoConfig {

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
