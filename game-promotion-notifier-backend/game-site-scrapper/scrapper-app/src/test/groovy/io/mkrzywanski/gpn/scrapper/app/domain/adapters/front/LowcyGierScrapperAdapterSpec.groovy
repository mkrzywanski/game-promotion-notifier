package io.mkrzywanski.gpn.scrapper.app.domain.adapters.front

import io.mkrzywanski.gpn.scrapper.app.GameSiteScrapperApp

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.LowcyGierScrapperService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import spock.lang.Specification

import java.time.Duration

import static org.mockito.Mockito.*

@SpringBootTest(properties = ["gpn.scheduling.cron=*/1 * * * * *"], classes = [GameSiteScrapperApp])
class LowcyGierScrapperAdapterSpec extends Specification {

    private static final Duration TWO_SECONDS = Duration.ofSeconds(2)

    @Autowired
    LowcyGierScrapperAdapter lowcyGierScrapperAdapter

    @Autowired
    LowcyGierScrapperService lowcyGierScrapperService;

    def "should invoked scrap"() {
        when: "scheduler is started"

        then:
        scrapperIsInvokedAtLeastOnce()
    }

    private void scrapperIsInvokedAtLeastOnce() {
        verify(lowcyGierScrapperService, timeout(TWO_SECONDS.toMillis()).atLeastOnce()).scrap()
    }

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        LowcyGierScrapperService lowcyGierScrapperService() {
            mock(LowcyGierScrapperService)
        }
    }
}
