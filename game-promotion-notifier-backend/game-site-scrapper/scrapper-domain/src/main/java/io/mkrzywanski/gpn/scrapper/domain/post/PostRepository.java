package io.mkrzywanski.gpn.scrapper.domain.post;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PostRepository {
    List<Hash> findByHashIn(Set<Hash> hashes);
    List<Post> saveAll(Collection<Post> postEntities);
}
