package io.mkrzywanski.pn.subscription.contract

import io.mkrzywanski.pn.subscription.Match
import io.mkrzywanski.pn.subscription.SubscriptionMatchingResult
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionEndpoint
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionFacade
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import spock.lang.Specification

class SubscriptionCreateBase extends Specification {

    def subscriptionFacade = Stub(SubscriptionFacade)
    def subscriptionEndpoint = new SubscriptionEndpoint(subscriptionFacade)

    void setup() {
        def userId = UUID.fromString("c6d5ea09-e52d-4a4d-a365-fd95142a99be")
        def subscriptionCreateRequest = new CreateSubscriptionRequest(userId, Set.of("Rainbow Six"))
        subscriptionFacade.create(subscriptionCreateRequest) >> fakeSubscriptionCreatedResponse()
        RestAssuredMockMvc.standaloneSetup(createSetup())
    }

    private StandaloneMockMvcBuilder createSetup() {
        MockMvcBuilders.standaloneSetup(subscriptionEndpoint)
    }

    private static SubscriptionCreatedResponse fakeSubscriptionCreatedResponse() {
        new SubscriptionCreatedResponse(UUID.randomUUID())
    }
}
