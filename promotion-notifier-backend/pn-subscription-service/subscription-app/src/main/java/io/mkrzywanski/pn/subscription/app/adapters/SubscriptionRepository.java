package io.mkrzywanski.pn.subscription.app.adapters;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import io.mkrzywanski.pn.subscription.Match;
import io.mkrzywanski.pn.subscription.MatchingRequest;
import io.mkrzywanski.pn.subscription.Offer;
import io.mkrzywanski.pn.subscription.Post;
import io.mkrzywanski.pn.subscription.SubscriptionCreateInfo;
import io.mkrzywanski.pn.subscription.SubscriptionId;
import io.mkrzywanski.pn.subscription.SubscriptionMatchingResult;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SubscriptionRepository implements io.mkrzywanski.pn.subscription.SubscriptionRepository {

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
            final NativeQuery query = getQueryForOfferText(offer.getText());
            return operations.search(query, SubscriptionElasticModel.class, indexCoordinates)
                    .stream()
                    .map(toMatch(postId, offer));
        };
    }

    //this can be optimized to use bulk api to peform several queries in one request
    private NativeQuery getQueryForOfferText(final String text) {
        final NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder()
                .withQuery(org.springframework.data.elasticsearch.client.elc.QueryBuilders.matchQueryAsQuery("subscriptions", text, Operator.And, 0f));
        return new NativeQuery(nativeQueryBuilder);
    }

    private Function<SearchHit<SubscriptionElasticModel>, Match> toMatch(final UUID postId, final Offer offer) {
        return hit -> new Match(hit.getContent().getUserId(), postId, offer.getId());
    }
}
