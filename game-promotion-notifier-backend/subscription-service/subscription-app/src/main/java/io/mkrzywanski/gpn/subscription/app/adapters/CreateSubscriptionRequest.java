package io.mkrzywanski.gpn.subscription.app.adapters;

import java.util.Set;
import java.util.UUID;

public class CreateSubscriptionRequest {
    private UUID userId;
    private Set<SubscriptionItem> itemSet;
}
