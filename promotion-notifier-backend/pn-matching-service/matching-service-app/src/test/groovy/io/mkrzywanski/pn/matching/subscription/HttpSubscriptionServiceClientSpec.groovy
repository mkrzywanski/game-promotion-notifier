package io.mkrzywanski.pn.matching.subscription

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class HttpSubscriptionServiceClientSpec extends Specification {
    def url = "http://subscription-service"
    def restTemplateMock = Stub(RestTemplate)
    def client = new HttpSubscriptionServiceClient(url, restTemplateMock)
    def userId = UUID.randomUUID()
    def postId = UUID.randomUUID()
    def offerId = UUID.randomUUID()
    def matchingRequest = new MatchingRequest(Set.of(new PostData(postId, List.of(new OfferData(offerId, "test")))))
    def matchingResponse = new MatchingResponse(Set.of(new Match(userId, postId, offerId)))
    def actualMatchingResponse

    def "should retrieve user details"() {
        given:
        subscriptionServiceCanReturnMatchingResponse()
        when:
        subscriptionServiceIsCalled()
        then:
        matchingResponseIsAsExpected()
    }

    private void subscriptionServiceCanReturnMatchingResponse() {
        restTemplateMock.postForObject("${url}/v1/subscriptions/match", matchingRequest, MatchingResponse.class) >> matchingResponse
    }

    private boolean matchingResponseIsAsExpected() {
        actualMatchingResponse == matchingResponse
    }

    private void subscriptionServiceIsCalled() {
        actualMatchingResponse = client.match(matchingRequest)
    }
}
