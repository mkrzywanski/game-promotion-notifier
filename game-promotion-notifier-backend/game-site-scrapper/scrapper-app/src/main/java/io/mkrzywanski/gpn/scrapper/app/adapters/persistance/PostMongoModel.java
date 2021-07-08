package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostId;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = "posts")
@Getter
public class PostMongoModel {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String hash;

    private String source;
    private Collection<GameOfferModel> gameOfferEntities;
    private Instant datePosted;

    public PostMongoModel(final UUID id, final String hash, final String source, final Collection<GameOfferModel> gameOfferEntities, final Instant datePosted) {
        this.id = id;
        this.hash = hash;
        this.source = source;
        this.gameOfferEntities = gameOfferEntities;
        this.datePosted = datePosted;
    }

    static PostMongoModel fromDomain(final Post post) {
        final List<GameOfferModel> gameOffers = post.getGameOffers()
                .stream()
                .map(GameOfferModel::fromDomain)
                .collect(Collectors.toList());
        return new PostMongoModel(post.getPostId().getId(), post.getHash().asString(), post.getSource(), gameOffers, post.getDatePosted().toInstant());
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "hash='" + hash + '\'' +
                ", source='" + source + '\'' +
                ", gameOfferEntities=" + gameOfferEntities +
                '}';
    }

    Post toDomain() {
        final Hash hash = Hash.fromString(this.hash);
        final List<GameOffer> gameOffers = this.gameOfferEntities.stream()
                .map(GameOfferModel::toDomain)
                .toList();
        return new Post(PostId.from(this.id), hash, this.source, gameOffers, null);
    }

//    UUID getId() {
//        return UUID.fromString(id);
//    }
}
