package io.mkrzywanski.gpn.scrapper.app.integ

import com.github.tomakehurst.wiremock.WireMockServer
import io.mkrzywanski.gpn.scrapper.app.adapters.GameHunterScrapperAdapter
import io.mkrzywanski.gpn.scrapper.app.adapters.persistance.MongoPostRepository
import io.mkrzywanski.gpn.scrapper.app.adapters.persistance.PostTransactionalOutboxMongoRepository
import io.mkrzywanski.gpn.scrapper.app.infra.JacksonConfig
import io.mkrzywanski.gpn.scrapper.app.infra.SchedulingConfig
import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrappingService
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.scheduling.annotation.EnableScheduling

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

@Configuration
@Import([GameHunterScrapperAdapter, SchedulingConfig, MongoPostRepository, JacksonConfig])
class ScrappingITConfig extends AbstractIntegrationConfig {

    private static final Instant ONE_DAY_AFTER_SCRAPING = LocalDate.of(2021, 6, 7).atStartOfDay().toInstant(ZoneOffset.UTC)

    private final Clock clock = Clock.fixed(ONE_DAY_AFTER_SCRAPING, ZoneId.of("UTC"))

    @Bean
    WireMockServer wireMockServer() {
        def wireMockServer = new WireMockServer(wireMockConfig().dynamicPort())
        wireMockServer.start()
        wireMockServer
    }

    @Bean
    GameHunterScrappingService scrapperService(WireMockServer wireMockServer, MongoOperations mongoOperations) {
        return GameHunterScrappingService.newInstance("http://localhost:" + wireMockServer.port(), new MongoPostRepository(mongoOperations), new PostTransactionalOutboxMongoRepository(mongoOperations), clock)
    }
}
