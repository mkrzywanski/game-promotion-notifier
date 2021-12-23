package io.mkrzywanski.pn.subscription.app.contracts

import io.mkrzywanski.pn.subscription.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito

abstract class SubscriptionMatchBase extends SubscriptionAbstractBase {
    @BeforeEach
    void setup() {
        def matchingRequest = new MatchingRequest(Set.of(new Post(UUID.fromString("b7b4c294-f8af-41e0-af4c-37eda04a6e65"), List.of(new Offer("Rainbow Six", 1)))))
        def result = new SubscriptionMatchingResult(Set.of(new Match(UUID.fromString("c5177efd-3739-4ed5-bb4c-23f9ca350107"), UUID.fromString("b7b4c294-f8af-41e0-af4c-37eda04a6e65"), 1)))
        Mockito.when(subscriptionFacade.match(matchingRequest)).thenReturn(result)
    }
}
