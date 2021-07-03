package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class PostTransactionalOutboxMongoRepository implements PostTransactionalOutboxRepository {

    private final MongoOperations mongoOperations;

    public PostTransactionalOutboxMongoRepository(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void put(final Set<Post> newPosts) {
        newPosts.stream()
                .map(post -> new NewPostOutboxMessage(post.getPostId().getId(), Instant.now()))
                .forEach(mongoOperations::save);
    }
}
