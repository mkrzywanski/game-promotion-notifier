package io.mkrzywanski.pn.scrapper.app.infra;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.GameHunterScrappingService;
import io.mkrzywanski.gpn.scrapper.domain.post.NewPostPublishing;
import io.mkrzywanski.gpn.scrapper.domain.post.PostPublisher;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BeanConfig {

    @Bean
    GameHunterScrappingService gameHunterScrapperService(final @Value("${gpn.gamehunter.url}") String gameHunterUrl,
                                                         final PostRepository postRepository,
                                                         final PostTransactionalOutboxRepository postTransactionalOutboxRepository) {
        return GameHunterScrappingService.newInstance(gameHunterUrl, postRepository, postTransactionalOutboxRepository, Clock.systemDefaultZone());
    }

    @Bean
    NewPostPublishing newPostPublishing(final PostTransactionalOutboxRepository postTransactionalOutboxRepository,
                                        final PostRepository postRepository,
                                        final PostPublisher postPublisher) {
        return new NewPostPublishing(postTransactionalOutboxRepository, postRepository, postPublisher);
    }
}
