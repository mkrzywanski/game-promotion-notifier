package io.mkrzywanski.pn.subscription;

public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(final SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionId createSubscription(final SubscriptionCreateInfo subscriptionCreateInfo) {
        return subscriptionRepository.create(subscriptionCreateInfo);
    }


    public SubscriptionMatchingResult match(final MatchingRequest matchingRequest) {
        return subscriptionRepository.match(matchingRequest);
    }
}
