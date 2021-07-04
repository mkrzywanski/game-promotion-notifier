package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public Set<PostId> poll() {
        return mongoOperations.findAll(NewPostOutboxMessage.class)
                .stream()
                .map(NewPostOutboxMessage::getPostId)
                .map(PostId::from)
                .collect(Collectors.toSet());
    }
}
