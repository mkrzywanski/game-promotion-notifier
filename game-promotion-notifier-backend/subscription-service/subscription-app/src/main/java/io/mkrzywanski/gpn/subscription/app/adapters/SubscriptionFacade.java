package io.mkrzywanski.gpn.subscription.app.adapters;

import io.mkrzywanski.gpn.subscription.*;
import io.mkrzywanski.gpn.subscription.app.api.SubscriptionItem;
import io.mkrzywanski.gpn.subscription.app.api.CreateSubscriptionRequest;
import io.mkrzywanski.gpn.subscription.app.api.SubscriptionCreatedResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    SubscriptionFacade(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    SubscriptionCreatedResponse create(final CreateSubscriptionRequest request) {
        final UserId userId = UserId.of(request.getUserId());
        final SubscriptionCreateInfo subscriptionCreateInfo = new SubscriptionCreateInfo(userId, extractSubscriptionItems(request));
        final SubscriptionId subscription = subscriptionService.createSubscription(subscriptionCreateInfo);
        return new SubscriptionCreatedResponse(subscription.asUuid());
    }

    SubscriptionMatchingResult match(final MatchingRequest matchingRequest) {
        return subscriptionService.match(matchingRequest);
    }

    private Set<SubscriptionEntry> extractSubscriptionItems(final CreateSubscriptionRequest request) {
        return request.getItemSet().stream().map(toSubscriptionEntry()).collect(Collectors.toSet());
    }

    private Function<SubscriptionItem, SubscriptionEntry> toSubscriptionEntry() {
        return subscriptionItem -> new SubscriptionEntry(subscriptionItem.getValue());
    }
}
