package io.mkrzywanski.gpn.scrapper.app.domain.adapters.front;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GameHunterScrapperAdapter {

    private final GameHunterScrapperService gameHunterScrapperService;

    public GameHunterScrapperAdapter(final GameHunterScrapperService gameHunterScrapperService) {
        this.gameHunterScrapperService = gameHunterScrapperService;
    }

    @Scheduled(cron = "${gpn.scheduling.cron}")
//    @Scheduled(fixedRate = 100)
    public void scrap() {
        gameHunterScrapperService.scrap();
    }
}
