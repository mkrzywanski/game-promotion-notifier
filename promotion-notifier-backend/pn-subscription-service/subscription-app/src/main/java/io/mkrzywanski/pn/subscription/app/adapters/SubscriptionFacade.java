package io.mkrzywanski.pn.subscription.app.adapters;

import io.mkrzywanski.pn.subscription.*;
import io.mkrzywanski.pn.subscription.app.api.SubscriptionItem;
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest;
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    SubscriptionFacade(final SubscriptionService subscriptionService) {
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
        return request.getItemSet().stream().map(toSubscriptionEntry()).collect(Collectors.toSet());
    }

    private Function<SubscriptionItem, SubscriptionEntry> toSubscriptionEntry() {
        return subscriptionItem -> new SubscriptionEntry(subscriptionItem.getValue());
    }
}
