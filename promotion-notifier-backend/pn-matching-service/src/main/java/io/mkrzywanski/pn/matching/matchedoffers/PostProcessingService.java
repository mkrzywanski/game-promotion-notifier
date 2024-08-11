package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
class PostProcessingService {

    private final PostsProcessor postsProcessor;
    private final MatchesRepository matchesRepository;

    PostProcessingService(final PostsProcessor postsProcessor, final MatchesRepository matchesRepository) {
        this.postsProcessor = postsProcessor;
        this.matchesRepository = matchesRepository;
    }

    @Transactional
    void process(final Post post) {
        final var matches = postsProcessor.process(post);
        log.info("Matches generated {}", matches);
        matchesRepository.saveOrUpdate(matches);
    }
}
