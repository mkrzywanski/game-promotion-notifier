package io.mkrzywanski.pn.subscription;

public interface SubscriptionRepository {
    SubscriptionId create(SubscriptionCreateInfo subscriptionCreateInfo);

    SubscriptionMatchingResult match(MatchingRequest matchingRequest);
}
