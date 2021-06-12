package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameHunterScrapperService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameHunterScrapperService.class);

    private final PostRepository postRepository;
    private final GameHunterScrapper gameHunterScrapper;
    private final int minimalDaysInterval = 2;

    GameHunterScrapperService(final GameHunterScrapper gameHunterScrapper,
                              final PostRepository postRepository) {
        this.postRepository = postRepository;
        this.gameHunterScrapper = gameHunterScrapper;
    }

    public static GameHunterScrapperService newInstance(final String serviceUrl, final PostRepository postRepository) {
        return new GameHunterScrapperService(new GameHunterScrapper(new GameHunterClient(serviceUrl), new GameHunterParser()), postRepository);
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

            LOGGER.info("New posts to be saged from page {} : {}", pageNumber, newPostsFromCurrentPage);
            allNewPosts.addAll(newPostsFromCurrentPage);

            pageNumber++;

        } while (!newPostHashes.isEmpty());

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
        return post.isYoungerThan(minimalDaysInterval);
    }
}
