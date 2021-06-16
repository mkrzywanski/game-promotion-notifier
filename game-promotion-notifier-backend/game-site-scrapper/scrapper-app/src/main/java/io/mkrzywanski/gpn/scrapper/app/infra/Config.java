package io.mkrzywanski.gpn.scrapper.app.infra;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrapperService;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class Config {

    @Bean
    GameHunterScrapperService gameHunterScrapperService(final @Value("${gpn.gamehunter.url}") String gameHunterUrl,
                                                        final PostRepository postRepository) {
        return GameHunterScrapperService.newInstance(gameHunterUrl, postRepository, Clock.systemDefaultZone());
    }
}
