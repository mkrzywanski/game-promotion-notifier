package io.mkrzywanski.gpn.subscription;

public interface SubscriptionRepository {
    SubscriptionId create(SubscriptionCreateInfo subscriptionCreateInfo);
}
