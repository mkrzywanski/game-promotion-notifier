package io.mkrzywanski.gpn.scrapper.app.adapters.publishing;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class PostExternalModel {
    UUID postId;
    Collection<GameOfferExternalModel> gameOffers;

    static PostExternalModel fromDomain(final Post post) {
        final List<GameOfferExternalModel> objectStream = post.getGameOffers().stream().map(GameOfferExternalModel::fromDomain).collect(Collectors.toList());
        return new PostExternalModel(post.getPostId().getId(), objectStream);
    }
}
