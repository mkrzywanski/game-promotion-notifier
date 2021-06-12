package io.mkrzywanski.gpn.scrapper.app.infra;

import io.mkrzywanski.gpn.scrapper.domain.gamehunter.LowcyGierScrapperService;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {

    private final String lowcyGierUrl;

    Config(@Value("${gpn.lowcy.url}") final String lowcyGierUrl) {
        this.lowcyGierUrl = lowcyGierUrl;
    }

    @Bean
    LowcyGierScrapperService lowcyGierScrapperFacade(final PostRepository postRepository) {
        return LowcyGierScrapperService.newInstance(lowcyGierUrl, postRepository);
    }
}
