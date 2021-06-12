package io.mkrzywanski.gpn.scrapper.app.infra;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {

    private final String gameHunterUrl;

    Config(@Value("${gpn.gamehunter.url}") final String gameHunterUrl) {
        this.gameHunterUrl = gameHunterUrl;
    }

    @Bean
    GameHunterScrapperService gameHunterScrapperService(final PostRepository postRepository) {
        return GameHunterScrapperService.newInstance(gameHunterUrl, postRepository);
    }
}
