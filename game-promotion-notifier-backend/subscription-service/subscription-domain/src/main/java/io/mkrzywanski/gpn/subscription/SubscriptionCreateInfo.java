package io.mkrzywanski.gpn.subscription;

import java.util.Set;
import java.util.stream.Collectors;

public class SubscriptionCreateInfo {

    private final UserId userId;
    private final Set<SubscriptionEntry> subscriptionEntrySet;

    public SubscriptionCreateInfo(final UserId userId, final Set<SubscriptionEntry> subscriptionEntrySet) {
        this.userId = userId;
        this.subscriptionEntrySet = subscriptionEntrySet;
    }

    public UserId getUserId() {
        return userId;
    }

    public Set<String> entriesAsStrings() {
        return subscriptionEntrySet.stream().map(SubscriptionEntry::asString).collect(Collectors.toSet());
    }
}
