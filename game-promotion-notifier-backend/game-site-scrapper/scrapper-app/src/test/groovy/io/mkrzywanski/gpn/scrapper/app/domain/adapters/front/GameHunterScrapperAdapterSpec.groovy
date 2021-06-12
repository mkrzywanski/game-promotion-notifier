package io.mkrzywanski.gpn.scrapper.app.domain.adapters.front


import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import spock.lang.Specification

import java.time.Duration

import static org.mockito.Mockito.*

@SpringBootTest(properties = ["gpn.scheduling.cron=*/1 * * * * *"])
class GameHunterScrapperAdapterSpec extends Specification {

    private static final Duration TWO_SECONDS = Duration.ofSeconds(2)

    @Autowired
    GameHunterScrapperService gameHunterScrapperService;

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
        GameHunterScrapperService gameHunterScrapperServiceMock() {
            mock(GameHunterScrapperService)
        }

        @Bean
        GameHunterScrapperAdapter gameHunterScrapperAdapter(GameHunterScrapperService gameHunterScrapperService) {
            new GameHunterScrapperAdapter(gameHunterScrapperService)
        }
    }
}
