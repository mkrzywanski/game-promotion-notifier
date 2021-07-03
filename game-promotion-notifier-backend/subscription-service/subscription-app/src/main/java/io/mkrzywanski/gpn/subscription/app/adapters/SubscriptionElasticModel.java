package io.mkrzywanski.gpn.subscription.app.adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SubscriptionElasticModel {
    private final UUID userId;
    private final Set<String> subscriptions;
}
