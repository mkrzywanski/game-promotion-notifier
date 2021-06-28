package io.mkrzywanski.gpn.subscription.app.adapters;

import io.mkrzywanski.gpn.subscription.*;
import org.elasticsearch.index.query.Operator;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class SubscriptionRepository implements io.mkrzywanski.gpn.subscription.SubscriptionRepository {

    private final ElasticsearchOperations operations;
    private final IndexCoordinates indexCoordinates;

    public SubscriptionRepository(final ElasticsearchOperations operations, final String indexName) {
        this.operations = operations;
        this.indexCoordinates = IndexCoordinates.of(indexName);
    }

    @Override
    public SubscriptionId create(final SubscriptionCreateInfo subscriptionCreateInfo) {
        final UUID subscriptionId = UUID.randomUUID();
        final Set<String> entries = subscriptionCreateInfo.entriesAsStrings();
        final SubscriptionElasticModel subscriptionElasticModel = new SubscriptionElasticModel(subscriptionCreateInfo.getUserId().asUuid(), entries);

        final IndexQuery query = new IndexQueryBuilder()
                .withId(subscriptionId.toString())
                .withObject(subscriptionElasticModel)
                .build();
        operations.index(query, indexCoordinates);
        return new SubscriptionId(subscriptionId);
    }

    @Override
    public SubscriptionMatchingResult match(final MatchingRequest matchingRequest) {
        final Set<Post> postsToMatch = matchingRequest.getPostsToMatch();
        final Set<Match> matches = getMatches(postsToMatch);
        return new SubscriptionMatchingResult(matches);
    }

    private Set<Match> getMatches(final Set<Post> postsToMatch) {
        return postsToMatch.stream().flatMap(getMatchesForPost()).collect(Collectors.toSet());
    }

    private Function<Post, Stream<? extends Match>> getMatchesForPost() {
        return post -> post.getOffers().stream().flatMap(toMatches(post.getPostId()));
    }

    private Function<Offer, Stream<Match>> toMatches(final UUID postId) {
        return offer -> {
            final NativeSearchQuery query = getQueryForOfferText(offer.getText());
            return operations.search(query, SubscriptionElasticModel.class, indexCoordinates)
                    .stream()
                    .map(toMatch(postId, offer));
        };
    }

    //this can be optimized to use bulk api to peform several queries in one request
    private NativeSearchQuery getQueryForOfferText(final String text) {
        return new NativeSearchQueryBuilder()
                .withQuery(matchQuery("subscriptions", text).operator(Operator.AND))
                .build();
    }

    private Function<SearchHit<SubscriptionElasticModel>, Match> toMatch(final UUID postId, final Offer offer) {
        return hit -> new Match(hit.getContent().getUserId(), postId, offer.getId());
    }
}
