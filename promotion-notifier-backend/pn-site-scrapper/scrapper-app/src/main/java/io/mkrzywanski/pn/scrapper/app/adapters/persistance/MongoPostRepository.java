package io.mkrzywanski.pn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class MongoPostRepository implements PostRepository {

    private final MongoOperations mongoOperations;

    public MongoPostRepository(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Hash> findByHashIn(final Set<Hash> hashes) {
        final List<String> hashesStrings = hashes.stream().map(Hash::asString).toList();
        final Query query = Query.query(Criteria.where("hash").in(hashesStrings));
        query.fields().include("id");
        return mongoOperations.findDistinct("hash", PostMongoModel.class, String.class)
                .stream()
                .map(Hash::fromString)
                .toList();
    }

    @Override
    public List<Post> saveAll(final Collection<Post> posts) {
        return posts.stream()
                .map(PostMongoModel::fromDomain)
                .map(mongoOperations::save)
                .map(PostMongoModel::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByIds(final Set<PostId> postIds) {
        final Set<UUID> rawPostIds = postIds.stream()
                .map(PostId::getId)
                .collect(Collectors.toSet());
        final Query query = Query.query(Criteria.where("id").in(rawPostIds));
        return mongoOperations.find(query, PostMongoModel.class)
                .stream()
                .map(PostMongoModel::toDomain)
                .toList();
    }
}
