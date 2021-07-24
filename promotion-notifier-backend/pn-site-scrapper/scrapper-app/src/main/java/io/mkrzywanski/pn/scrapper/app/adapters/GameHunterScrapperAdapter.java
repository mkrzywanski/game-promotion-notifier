package io.mkrzywanski.pn.scrapper.app.adapters;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrappingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GameHunterScrapperAdapter {

    private final GameHunterScrappingService gameHunterScrappingService;

    public GameHunterScrapperAdapter(final GameHunterScrappingService gameHunterScrappingService) {
        this.gameHunterScrappingService = gameHunterScrappingService;
    }

    @Scheduled(cron = "${gpn.scheduling.scrapping.cron}")
    @Transactional
    public void scrap() {
        gameHunterScrappingService.scrap();
    }
}
