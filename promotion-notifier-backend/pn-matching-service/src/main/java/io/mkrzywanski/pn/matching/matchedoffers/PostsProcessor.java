package io.mkrzywanski.pn.matching.matchedoffers;

import io.mkrzywanski.pn.matching.subscription.*;
import io.mkrzywanski.pn.matching.subscription.api.Match;
import io.mkrzywanski.pn.matching.subscription.api.MatchingRequest;
import io.mkrzywanski.pn.matching.subscription.api.MatchingResponse;
import io.mkrzywanski.pn.matching.subscription.api.OfferData;
import io.mkrzywanski.pn.matching.subscription.api.PostData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class PostsProcessor {

    private final SubscriptionServiceClient subscriptionServiceClient;

    PostsProcessor(final SubscriptionServiceClient subscriptionServiceClient) {
        this.subscriptionServiceClient = subscriptionServiceClient;
    }

    List<UserOfferMatches> process(final Post post) {
        final var postData = toPostData(post);
        final var matchingResponse = getMatchesForPost(postData);
        final var matches = matchingResponse.getMatches();

        final var userMatches = matches.stream().collect(Collectors.groupingBy(Match::getUserId));

        return userMatches.entrySet()
                .stream()
                .map(extractUserMatchesFor(post))
                .collect(Collectors.toList());
    }

    private Function<Map.Entry<UUID, List<Match>>, UserOfferMatches> extractUserMatchesFor(final Post post) {
        return entry -> {
            final var userId = entry.getKey();
            final var matches = entry.getValue().stream().collect(Collectors.groupingBy(Match::getPostId))
                    .values()
                    .stream()
                    .map(toPostEntity(post))
                    .collect(Collectors.toSet());

            return new UserOfferMatches(userId, matches);
        };
    }

    private Function<List<Match>, PostEntity> toPostEntity(final Post post) {
        return matches -> {
            final var offers = matches
                    .stream()
                    .map(Match::getOfferId)
                    .map(uuid -> post.getOfferById(uuid).orElseThrow())
                    .map(this::getOfferEntity)
                    .collect(Collectors.toSet());
            return new PostEntity(post.getId(), post.getLink(), offers);
        };
    }

    private OfferEntity getOfferEntity(final Offer offer) {
        return new OfferEntity(offer.getName(), offer.getGamePrice(), offer.getLink());
    }

    private MatchingResponse getMatchesForPost(final PostData postData) {
        final var matchingRequest = new MatchingRequest(Set.of(postData));
        return subscriptionServiceClient.match(matchingRequest);
    }

    private PostData toPostData(final Post post) {
        return new PostData(post.getId(), post.getOffers().stream().map(offer -> new OfferData(offer.getId(), offer.getName())).collect(Collectors.toList()));

    }
}
