package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;

import java.util.function.Predicate;

interface PostFilter extends Predicate<Post> {
    String name();
}
