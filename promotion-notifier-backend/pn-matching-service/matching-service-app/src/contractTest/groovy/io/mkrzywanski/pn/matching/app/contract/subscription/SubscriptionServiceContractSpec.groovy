package io.mkrzywanski.pn.matching.app.contract.subscription

import io.mkrzywanski.pn.matching.infra.http.RestTemplateConfig
import io.mkrzywanski.pn.matching.subscription.HttpSubscriptionServiceClient
import io.mkrzywanski.pn.matching.subscription.Match
import io.mkrzywanski.pn.matching.subscription.MatchingRequest
import io.mkrzywanski.pn.matching.subscription.OfferData
import io.mkrzywanski.pn.matching.subscription.PostData
import io.mkrzywanski.pn.matching.subscription.SubscriptionServiceClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [RestTemplateConfig])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:subscription-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
@EnableAutoConfiguration
class SubscriptionServiceContractSpec extends Specification {

    @StubRunnerPort("io.mkrzywanski:subscription-app")
    int producerPort

    @Autowired
    RestTemplate restTemplate

    SubscriptionServiceClient subscriptionServiceClient

    void setup() {
        subscriptionServiceClient = new HttpSubscriptionServiceClient("http://localhost:${producerPort}", restTemplate)
    }


    def 'should get user details'() {
        given:
        def postId = UUID.fromString("d02efa49-0d6f-4f17-9aaa-806ceb648477")
        def offerId = UUID.fromString("6c9bfd13-d071-4bd3-a028-53d44496e03b")
        def userId = UUID.fromString("7b3a0fb4-d94f-4cb1-85e0-c0219111afea")
        def request = new MatchingRequest(Set.of(new PostData(postId, List.of(new OfferData(offerId, "Rainbow Six")))))

        when:
        def matchingResponse = subscriptionServiceClient.match(request)

        then:
        matchingResponse != null
        with(matchingResponse) {
            matches.size() == 1
            matches.contains(new Match(userId, postId, offerId))
        }
    }
//
//    def "should throw exception when user could not be found"() {
//        given:
//
//        when:
//        userServiceClient.getUserDetails(NON_EXISTING_USER)
//
//        then:
//        def e = thrown(ClientCommunicationException)
//        with(e.errorResponse) {
//            it.status == 404
//            it.path == "/v1/users/${NON_EXISTING_USER}"
//            it.serviceName == 'user-service'
//            it.timestamp != null
//        }
//    }
}
