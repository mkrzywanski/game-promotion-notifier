package io.mkrzywanski.gpn.scrapper.app.domain.adapters.front;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.LowcyGierScrapperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LowcyGierScrapperAdapter {

    private final LowcyGierScrapperService lowcyGierScrapperService;

    LowcyGierScrapperAdapter(final LowcyGierScrapperService lowcyGierScrapperService) {
        this.lowcyGierScrapperService = lowcyGierScrapperService;
    }

    @Scheduled(cron = "${gpn.scheduling.cron}")
    void scrap() {
        lowcyGierScrapperService.scrap();
    }
}
