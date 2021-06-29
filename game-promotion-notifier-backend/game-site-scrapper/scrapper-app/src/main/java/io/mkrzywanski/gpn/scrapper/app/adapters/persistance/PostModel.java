package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.Hash;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Document(collection = "posts")
public class PostModel {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String hash;

    private String source;
    private Collection<GameOfferModel> gameOfferEntities;
    private Instant datePosted;

    public PostModel(final String hash, final String source, final Collection<GameOfferModel> gameOfferEntities, final Instant datePosted) {
        this.id = UUID.randomUUID();
        this.hash = hash;
        this.source = source;
        this.gameOfferEntities = gameOfferEntities;
        this.datePosted = datePosted;
    }

    String getHash() {
        return hash;
    }

    String getSource() {
        return source;
    }

    Collection<GameOfferModel> getGameOffers() {
        return gameOfferEntities;
    }

    public UUID getId() {
        return id;
    }

    static PostModel fromDomain(final Post post) {
        final List<GameOfferModel> gameOffers = post.getGameOffers()
                .stream()
                .map(GameOfferModel::fromDomain)
                .collect(Collectors.toList());
        return new PostModel(post.getHash().asString(), post.getSource(), gameOffers, post.getDatePosted().toInstant());
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "hash='" + hash + '\'' +
                ", source='" + source + '\'' +
                ", gameOfferEntities=" + gameOfferEntities +
                '}';
    }

    static Post toDomain(final PostModel postModel) {
        final Hash hash = Hash.fromString(postModel.hash);
        final List<GameOffer> gameOffers = postModel.gameOfferEntities.stream()
                .map(GameOfferModel::toDomain)
                .toList();
        return new Post(hash, postModel.source, gameOffers, null);
    }
}
