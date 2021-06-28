package io.mkrzywanski.gpn.subscription.app.adapters;

import java.util.Set;
import java.util.UUID;

public class SubscriptionElasticModel {
    private final UUID userId;
    private final Set<String> subscriptions;

    public SubscriptionElasticModel(final UUID userId, final Set<String> subscriptions) {
        this.userId = userId;
        this.subscriptions = subscriptions;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<String> getSubscriptions() {
        return subscriptions;
    }
}
