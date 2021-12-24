package io.mkrzywanski.pn.subscription.app.contracts

import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse
import io.mkrzywanski.pn.subscription.app.api.SubscriptionItem
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito

abstract class SubscriptionCreateBase extends SubscriptionAbstractBase {

    @BeforeEach
    void setup() {
        def userId = UUID.fromString("22e90bbd-7399-468a-9b76-cf050ff16c63")
        def request = new CreateSubscriptionRequest(userId, Set.of("Rainbow Six"))
        Mockito.when(subscriptionFacade.create(Mockito.eq(request))).thenReturn(new SubscriptionCreatedResponse(UUID.fromString("6d692849-58fd-439b-bb2c-50a5d3669fa9")))
    }
}