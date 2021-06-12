package io.mkrzywanski.gpn.scrapper.app.domain.adapters.front;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class GameHunterScrapperAdapter {

    private final GameHunterScrapperService gameHunterScrapperService;

    GameHunterScrapperAdapter(final GameHunterScrapperService gameHunterScrapperService) {
        this.gameHunterScrapperService = gameHunterScrapperService;
    }

    @Scheduled(cron = "${gpn.scheduling.cron}")
    void scrap() {
        gameHunterScrapperService.scrap();
    }
}
