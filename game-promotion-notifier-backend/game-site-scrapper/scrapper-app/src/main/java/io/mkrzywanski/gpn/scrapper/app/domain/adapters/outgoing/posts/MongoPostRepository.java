package io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts;

import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class MongoPostRepository implements PostRepository {

    private final MongoOperations mongoOperations;

    MongoPostRepository(final MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Hash> findByHashIn(final Set<Hash> hashes) {
        final List<String> hashesStrings = hashes.stream().map(Hash::asString).toList();
        final Query query = Query.query(Criteria.where("hash").in(hashesStrings));
        query.fields().include("id");
        return mongoOperations.findDistinct("hash", PostModel.class, String.class)
                .stream()
                .map(Hash::fromString)
                .toList();
    }

    @Override
    public List<Post> saveAll(final Collection<Post> posts) {
        return posts.stream()
                .map(PostModel::fromDomain)
                .map(mongoOperations::save)
                .map(PostModel::toDomain)
                .collect(Collectors.toList());
    }
}
