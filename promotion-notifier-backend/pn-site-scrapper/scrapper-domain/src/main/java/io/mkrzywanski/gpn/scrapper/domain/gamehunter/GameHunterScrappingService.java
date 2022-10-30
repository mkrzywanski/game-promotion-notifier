package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GameHunterScrappingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHunterScrappingService.class);

    private final PostRepository postRepository;
    private final PostTransactionalOutboxRepository postTransactionalOutboxRepository;
    private final GameHunterScrapper gameHunterScrapper;
    private final PostListFilter postListFilter;

    GameHunterScrappingService(final GameHunterScrapper gameHunterScrapper,
                               final PostRepository postRepository,
                               final PostTransactionalOutboxRepository postTransactionalOutboxRepository, final Clock clock) {
        this.postRepository = postRepository;
        this.gameHunterScrapper = gameHunterScrapper;
        this.postTransactionalOutboxRepository = postTransactionalOutboxRepository;
        this.postListFilter = new PostListFilter(new NotTooOldFilter(2, clock));
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
            final List<Hash> alreadySavedPostHashes = postRepository.findByHashIn(scrappedPostHashes);

            newPostHashes = new HashSet<>(scrappedPostHashes);
            alreadySavedPostHashes.forEach(newPostHashes::remove);

            LOGGER.info("New post hashes {}", newPostHashes);
            LOGGER.info("Already saved post hashes {}", alreadySavedPostHashes);

            final List<Post> newPostsFromCurrentPage = scrappedPosts.stream()
                    .filter(post -> !alreadySavedPostHashes.contains(post.getHash()))
                    .toList();

            LOGGER.info("New posts to be saved from page {} : {}", pageNumber, newPostsFromCurrentPage);
            allNewPosts.addAll(newPostsFromCurrentPage);

            final boolean currentPageIsPartiallyScrapped = alreadySavedPostHashes.size() > 0;
            if (currentPageIsPartiallyScrapped) {
                break;
            }

            pageNumber++;

        } while (!Thread.currentThread().isInterrupted());

        final Set<Post> distinctByHash = allNewPosts.stream()
                .filter(distinctByKey(Post::getHash))
                .collect(Collectors.toSet());

        postRepository.saveAll(distinctByHash);
        postTransactionalOutboxRepository.put(distinctByHash);
    }

    private Set<Hash> extractHashes(final List<Post> scrappedPosts) {
        return scrappedPosts.stream()
                .map(Post::getHash)
                .collect(Collectors.toSet());
    }

    private List<Post> scrapPage(final int pageNumber) {
        final var posts = gameHunterScrapper.scrap(pageNumber);
        LOGGER.info("All posts on page = {}", pageNumber);
        final var posts1 = postListFilter.filter(posts);
        LOGGER.info("Posts after filtering out too old posts = {}", posts1.size());
        return posts1;
    }

    private static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
        final Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
