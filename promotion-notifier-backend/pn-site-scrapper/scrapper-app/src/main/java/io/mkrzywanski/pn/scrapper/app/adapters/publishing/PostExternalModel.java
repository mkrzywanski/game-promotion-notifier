package io.mkrzywanski.pn.scrapper.app.adapters.publishing;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class PostExternalModel {
    UUID id;
    Collection<OfferExternalModel> offers;

    static PostExternalModel fromDomain(final Post post) {
        final List<OfferExternalModel> objectStream = post.getGameOffers().stream().map(OfferExternalModel::fromDomain).collect(Collectors.toList());
        return new PostExternalModel(post.getPostId().getId(), objectStream);
    }
}
