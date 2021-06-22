package io.mkrzywanski.gpn.subscription.app.adapters;

import io.mkrzywanski.gpn.subscription.SubscriptionService;

public class SubscriptionFacade {
    private final SubscriptionService subscriptionService;

    public SubscriptionFacade(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
//
//    public SubscriptionId subscriptionId(CreateSubscriptionRequest request) {
//        SubscriptionCreateInfo subscriptionCreateInfo = new SubscriptionCreateInfo();
//        subscriptionService.createSubscription()
//    }
}
