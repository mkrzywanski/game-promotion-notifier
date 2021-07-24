package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import com.mongodb.client.result.DeleteResult;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
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

    @Override
    public void delete(final Set<PostId> newPostsIds) {
        final List<UUID> postIds = newPostsIds.stream().map(PostId::getId).toList();
        final Query query = new Query(Criteria.where("postId").in(postIds));
        final DeleteResult result = mongoOperations.remove(query, NewPostOutboxMessage.class);
        log.info("Remove acknowledged {} . Removed elements {}", result.wasAcknowledged(), result.getDeletedCount());
    }
}
