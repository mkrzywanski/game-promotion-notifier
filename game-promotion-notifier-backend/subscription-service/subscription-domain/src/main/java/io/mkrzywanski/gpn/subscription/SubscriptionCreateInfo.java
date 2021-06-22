package io.mkrzywanski.gpn.subscription;

import java.util.Set;

public class SubscriptionCreateInfo {

    private final UserId userId;
    private final Set<SubscriptionEntry> subscriptionEntrySet;

    public SubscriptionCreateInfo(final UserId userId, final Set<SubscriptionEntry> subscriptionEntrySet) {
        this.userId = userId;
        this.subscriptionEntrySet = subscriptionEntrySet;
    }

    UserId getUserId() {
        return userId;
    }

    Set<SubscriptionEntry> getSubscriptionEntrySet() {
        return subscriptionEntrySet;
    }
}
