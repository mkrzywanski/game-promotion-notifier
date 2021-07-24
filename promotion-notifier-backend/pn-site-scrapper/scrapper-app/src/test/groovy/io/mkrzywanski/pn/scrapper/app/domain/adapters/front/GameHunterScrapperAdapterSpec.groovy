package io.mkrzywanski.pn.scrapper.app.domain.adapters.front


import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrappingService
import io.mkrzywanski.pn.scrapper.app.adapters.GameHunterScrapperAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import spock.lang.Specification

import java.time.Duration

import static org.mockito.Mockito.*

@SpringBootTest(properties = ["gpn.scheduling.scrapping.cron=*/1 * * * * *"])
class GameHunterScrapperAdapterSpec extends Specification {

    private static final Duration TWO_SECONDS = Duration.ofSeconds(2)

    @Autowired
    GameHunterScrappingService gameHunterScrapperService;

    def "should invoke scrapper"() {
        when: "scheduler is started"

        then:
        scrapperIsInvokedAtLeastOnce()
    }

    private void scrapperIsInvokedAtLeastOnce() {
        verify(gameHunterScrapperService, timeout(TWO_SECONDS.toMillis()).atLeastOnce()).scrap()
    }

    @Configuration
    @EnableScheduling
    static class TestConfig {
        @Bean
        GameHunterScrappingService gameHunterScrapperServiceMock() {
            mock(GameHunterScrappingService)
        }

        @Bean
        GameHunterScrapperAdapter gameHunterScrapperAdapter(GameHunterScrappingService gameHunterScrapperService) {
            new GameHunterScrapperAdapter(gameHunterScrapperService)
        }
    }
}
