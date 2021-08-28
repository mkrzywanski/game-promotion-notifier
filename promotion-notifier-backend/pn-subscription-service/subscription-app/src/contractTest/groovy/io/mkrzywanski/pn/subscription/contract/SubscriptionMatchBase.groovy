package io.mkrzywanski.pn.subscription.contract

import io.mkrzywanski.pn.subscription.*
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionEndpoint
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionFacade
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import spock.lang.Specification

class SubscriptionMatchBase extends Specification {

    def postId = UUID.fromString("d02efa49-0d6f-4f17-9aaa-806ceb648477")
    def offerId = UUID.fromString("6c9bfd13-d071-4bd3-a028-53d44496e03b")
    def userId = UUID.fromString("7b3a0fb4-d94f-4cb1-85e0-c0219111afea")

    def subscriptionFacade = Stub(SubscriptionFacade)
    def subscriptionEndpoint = new SubscriptionEndpoint(subscriptionFacade)

    void setup() {
        def matchingRequest = new MatchingRequest(Set.of(new Post(postId, List.of(new Offer(offerId, "Rainbow Six")))))
        subscriptionFacade.match(matchingRequest) >> fakeSubscriptionMatchingResult()
        RestAssuredMockMvc.standaloneSetup(createSetup())
    }

    private StandaloneMockMvcBuilder createSetup() {
        MockMvcBuilders.standaloneSetup(subscriptionEndpoint)
    }

    private SubscriptionMatchingResult fakeSubscriptionMatchingResult() {
        new SubscriptionMatchingResult(Set.of(new Match(userId, postId, offerId)))
    }
}
