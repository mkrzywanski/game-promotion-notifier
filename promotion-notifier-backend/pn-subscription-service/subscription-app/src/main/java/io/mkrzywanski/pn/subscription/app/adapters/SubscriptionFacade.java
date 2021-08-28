package io.mkrzywanski.pn.subscription.app.adapters;

import io.mkrzywanski.pn.subscription.MatchingRequest;
import io.mkrzywanski.pn.subscription.SubscriptionCreateInfo;
import io.mkrzywanski.pn.subscription.SubscriptionEntry;
import io.mkrzywanski.pn.subscription.SubscriptionId;
import io.mkrzywanski.pn.subscription.SubscriptionMatchingResult;
import io.mkrzywanski.pn.subscription.SubscriptionService;
import io.mkrzywanski.pn.subscription.UserId;
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest;
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    public SubscriptionFacade(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public SubscriptionCreatedResponse create(final CreateSubscriptionRequest request) {
        final UserId userId = UserId.of(request.getUserId());
        final SubscriptionCreateInfo subscriptionCreateInfo = new SubscriptionCreateInfo(userId, extractSubscriptionItems(request));
        final SubscriptionId subscription = subscriptionService.createSubscription(subscriptionCreateInfo);
        return new SubscriptionCreatedResponse(subscription.asUuid());
    }

    public SubscriptionMatchingResult match(final MatchingRequest matchingRequest) {
        return subscriptionService.match(matchingRequest);
    }

    private Set<SubscriptionEntry> extractSubscriptionItems(final CreateSubscriptionRequest request) {
        return request.getItems().stream().map(SubscriptionEntry::new).collect(Collectors.toSet());
    }
}
