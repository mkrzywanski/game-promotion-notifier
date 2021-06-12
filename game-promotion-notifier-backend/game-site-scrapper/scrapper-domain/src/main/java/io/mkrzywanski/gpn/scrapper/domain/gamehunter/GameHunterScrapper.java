package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class GameHunterScrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHunterScrapperService.class);

    private final GameHunterClient gameHunterClient;
    private final GameHunterParser gameHunterParser;

    GameHunterScrapper(final GameHunterClient gameHunterClient,
                       final GameHunterParser gameHunterParser) {
        this.gameHunterClient = gameHunterClient;
        this.gameHunterParser = gameHunterParser;
    }

    List<Post> scrap(final int pageNumber) {
        try {
            final String pageHtml = gameHunterClient.getPage(pageNumber);
            return gameHunterParser.parse(pageHtml);
        } catch (final GameHunterClientException e) {
            LOGGER.info("Error while connection to site. {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}
