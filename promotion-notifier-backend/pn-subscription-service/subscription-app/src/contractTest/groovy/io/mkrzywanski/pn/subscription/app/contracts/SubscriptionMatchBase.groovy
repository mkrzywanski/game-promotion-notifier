package io.mkrzywanski.pn.subscription.app.contracts

import io.mkrzywanski.pn.subscription.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito

abstract class SubscriptionMatchBase extends SubscriptionAbstractBase {
    @BeforeEach
    void setup() {
        def offerId = UUID.fromString("ab5dd318-9d8e-4594-af1b-f800b22d7f24")
        def postId = UUID.fromString("b7b4c294-f8af-41e0-af4c-37eda04a6e65")
        def matchingRequest = new MatchingRequest(Set.of(new Post(postId, List.of(new Offer("Rainbow Six", offerId)))))
        def result = new SubscriptionMatchingResult(Set.of(new Match(UUID.fromString("c5177efd-3739-4ed5-bb4c-23f9ca350107"), postId, offerId)))
        Mockito.when(subscriptionFacade.match(matchingRequest)).thenReturn(result)
    }
}
