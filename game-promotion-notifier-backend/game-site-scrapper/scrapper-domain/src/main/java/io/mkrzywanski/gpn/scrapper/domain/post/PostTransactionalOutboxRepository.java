package io.mkrzywanski.gpn.scrapper.domain.post;

import java.util.Set;

public interface PostTransactionalOutboxRepository {
    void put(Set<Post> newPosts);
    Set<PostId> poll();
}
