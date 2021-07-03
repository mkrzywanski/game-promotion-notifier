package io.mkrzywanski.gpn.subscription.app.api;

import java.util.UUID;

public class SubscriptionCreatedResponse {

    private final UUID subscriptionId;

    public SubscriptionCreatedResponse(final UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public UUID asUuid() {
        return subscriptionId;
    }
}
