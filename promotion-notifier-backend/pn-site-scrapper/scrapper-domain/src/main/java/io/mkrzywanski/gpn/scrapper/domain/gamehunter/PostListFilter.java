package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class PostListFilter {

    private final PostFilter postFilter;

    PostListFilter(final PostFilter postFilter) {
        this.postFilter = postFilter;
    }

    List<Post> filter(final List<Post> posts) {
        log.info("Post amount before applying filter {}", postFilter.name());
        final var postsAfterFiltering = posts.stream()
                .filter(postFilter)
                .toList();
        log.info("Post amount after applying filter {}", postFilter.name());
        return postsAfterFiltering;
    }
}
