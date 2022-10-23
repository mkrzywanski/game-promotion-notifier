package io.mkrzywanski.pn.matching.matchedoffers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
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
        matchesRepository.saveOrUpdate(matches);
    }
}
