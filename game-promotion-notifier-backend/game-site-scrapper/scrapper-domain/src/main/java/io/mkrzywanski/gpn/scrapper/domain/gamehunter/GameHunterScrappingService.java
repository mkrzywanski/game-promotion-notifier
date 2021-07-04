package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameHunterScrappingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHunterScrappingService.class);

    private final PostRepository postRepository;
    private final PostTransactionalOutboxRepository postTransactionalOutboxRepository;
    private final GameHunterScrapper gameHunterScrapper;
    private final int minimalDaysInterval = 2;
    private final Clock clock;

    GameHunterScrappingService(final GameHunterScrapper gameHunterScrapper,
                               final PostRepository postRepository,
                               final PostTransactionalOutboxRepository postTransactionalOutboxRepository, final Clock clock) {
        this.postRepository = postRepository;
        this.gameHunterScrapper = gameHunterScrapper;
        this.postTransactionalOutboxRepository = postTransactionalOutboxRepository;
        this.clock = clock;
    }

    public static GameHunterScrappingService newInstance(final String serviceUrl, final PostRepository postRepository, final PostTransactionalOutboxRepository postTransactionalOutboxRepository, final Clock clock) {
        return new GameHunterScrappingService(new GameHunterScrapper(new GameHunterClient(serviceUrl), new GameHunterParser()), postRepository, postTransactionalOutboxRepository, clock);
    }

    public void scrap() {
        int pageNumber = 1;
        Set<Hash> newPostHashes;
        final List<Post> allNewPosts = new ArrayList<>();
        do {
            final List<Post> scrappedPosts = scrapPage(pageNumber);
            if (scrappedPosts.isEmpty()) {
                LOGGER.info("No fresh posts to scrap on page {}", pageNumber);
                break;
            }
            LOGGER.info("Scrapped page {} . Posts {}", pageNumber, scrappedPosts);
            final Set<Hash> scrappedPostHashes = extractHashes(scrappedPosts);
            final List<Hash> alreadySavedPosts = postRepository.findByHashIn(scrappedPostHashes);

            newPostHashes = new HashSet<>(scrappedPostHashes);
            alreadySavedPosts.forEach(newPostHashes::remove);

            LOGGER.info("New post hashes {}", newPostHashes);
            LOGGER.info("Already saved post hashes {}", alreadySavedPosts);

            final List<Post> newPostsFromCurrentPage = scrappedPosts.stream()
                    .filter(post -> !alreadySavedPosts.contains(post.getHash()))
                    .toList();

            LOGGER.info("New posts to be saved from page {} : {}", pageNumber, newPostsFromCurrentPage);
            allNewPosts.addAll(newPostsFromCurrentPage);

            final boolean currentPageIsPartiallyScrapped = alreadySavedPosts.size() > 0;
            if (currentPageIsPartiallyScrapped) {
                break;
            }

            pageNumber++;

        } while (true);

        postRepository.saveAll(allNewPosts);
    }

    private Set<Hash> extractHashes(final List<Post> scrappedPosts) {
        return scrappedPosts.stream()
                .map(Post::getHash)
                .collect(Collectors.toSet());
    }

    private List<Post> scrapPage(final int pageNumber) {
        return gameHunterScrapper.scrap(pageNumber)
                .stream()
                .filter(this::isNotTooOld)
                .toList();
    }

    private boolean isNotTooOld(final Post post) {
        return post.isYoungerThan(minimalDaysInterval, clock);
    }
}
