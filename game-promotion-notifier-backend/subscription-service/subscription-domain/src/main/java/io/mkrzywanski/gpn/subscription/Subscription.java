package io.mkrzywanski.gpn.subscription;

import java.util.Set;

public class Subscription {

    private final SubscriptionId subscriptionId;
    private final UserId userId;
    private final Set<SubscriptionEntry> subscriptionEntrySet;

    public Subscription(final SubscriptionId subscriptionId, final UserId userId, final Set<SubscriptionEntry> subscriptionEntrySet) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.subscriptionEntrySet = subscriptionEntrySet;
    }
}
