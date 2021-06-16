package io.mkrzywanski.gpn.scrapper.app.integ

import com.github.tomakehurst.wiremock.WireMockServer
import com.mongodb.ConnectionString
import io.mkrzywanski.gpn.scrapper.app.domain.adapters.front.GameHunterScrapperAdapter
import io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts.MongoPostRepository
import io.mkrzywanski.gpn.scrapper.app.infra.SpringConfig
import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.scheduling.annotation.EnableScheduling
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import([GameHunterScrapperAdapter, SpringConfig, MongoPostRepository])
class IntegrationTestConfig {

    @Autowired
    private Environment environment

    private static final Instant ONE_DAY_AFTER_SCRAPING = LocalDate.of(2021, 6, 7).atStartOfDay().toInstant(ZoneOffset.UTC)
    private final Clock clock = Clock.fixed(ONE_DAY_AFTER_SCRAPING, ZoneId.of("UTC"))

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

    @Bean
    WireMockServer wireMockServer() {
        def wireMockServer = new WireMockServer(wireMockConfig().dynamicPort())
        wireMockServer.start()
        wireMockServer
    }

    @Bean
    GameHunterScrapperService scrapperService(WireMockServer wireMockServer, MongoOperations mongoOperations) {
        return GameHunterScrapperService.newInstance("http://localhost:" + wireMockServer.port(), new MongoPostRepository(mongoOperations), clock)
    }
}
