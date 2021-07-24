package io.mkrzywanski.pn.scrapper.app.integ.scrapping

import com.github.tomakehurst.wiremock.WireMockServer
import io.mkrzywanski.pn.scrapper.app.adapters.GameHunterScrapperAdapter
import io.mkrzywanski.pn.scrapper.app.adapters.persistance.MongoPostRepository
import io.mkrzywanski.pn.scrapper.app.adapters.persistance.PostTransactionalOutboxMongoRepository
import io.mkrzywanski.pn.scrapper.app.infra.JacksonConfig
import io.mkrzywanski.pn.scrapper.app.infra.SchedulingConfig
import io.mkrzywanski.pn.scrapper.app.integ.AbstractIntegrationConfig
import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrappingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.core.MongoOperations

import java.time.*

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
    GameHunterScrappingService scrapperService(final WireMockServer wireMockServer, final MongoOperations mongoOperations) {
        return GameHunterScrappingService.newInstance("http://localhost:" + wireMockServer.port(), new MongoPostRepository(mongoOperations), new PostTransactionalOutboxMongoRepository(mongoOperations), clock)
    }
}
