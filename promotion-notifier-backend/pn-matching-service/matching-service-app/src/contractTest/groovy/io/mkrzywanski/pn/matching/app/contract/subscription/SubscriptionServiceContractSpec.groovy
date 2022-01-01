package io.mkrzywanski.pn.matching.app.contract.subscription


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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [TestConfig])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:subscription-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
@EnableAutoConfiguration
class SubscriptionServiceContractSpec extends Specification {

    @StubRunnerPort("io.mkrzywanski:subscription-app")
    int producerPort

    @Autowired
    WebClient webClient

    SubscriptionServiceClient subscriptionServiceClient

    void setup() {
        subscriptionServiceClient = new HttpSubscriptionServiceClient("http://localhost:${producerPort}", webClient)
    }


    def 'should get user details'() {
        given:
        def postId = UUID.fromString("b7b4c294-f8af-41e0-af4c-37eda04a6e65")
        def offerId = UUID.fromString("ab5dd318-9d8e-4594-af1b-f800b22d7f24")
        def userId = UUID.fromString("c5177efd-3739-4ed5-bb4c-23f9ca350107")
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
}

@Configuration
class TestConfig {
    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJERmJmZWVpR29ySXMyZ1VUaDA2d2hfZVZKTUc2azU5X0dVZzBrOVRLb05vIn0.eyJqdGkiOiJlZGY2OGQ2ZS03NjQzLTRjYjgtODU1NS0xOGNkM2M3Njc2ZTIiLCJleHAiOjIwMzA3ODA5MTMsIm5iZiI6MCwiaWF0IjoxNTU3ODI3MzEzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3NjUvYXV0aC9yZWFsbXMvc3ByaW5nX2Nsb3VkX2NvbnRyYWN0cyIsImF1ZCI6InNwcmluZ19jbG91ZF9jb250cmFjdHMiLCJzdWIiOiJjMzNhMzgxNi1lYWExLTRhYWMtYjdhMi1jNDc3NDRkODcwMDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdfY2xvdWRfY29udHJhY3RzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiOGM4ZThhMmMtZDQ5ZC00ZTYxLTlkNTYtOTBkZDVjYTQ1ZDNiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjdXN0b21lciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwic2NvcGUiOiIiLCJhdWQiOiJvYXV0aDItcmVzb3VyY2UiLCJ1c2VyX25hbWUiOiJvbGQgZW5vdWdoIiwidXNlcl9kZXRhaWxzIjp7ImFnZSI6NDIsInVzZXJuYW1lIjoib2xkIGVub3VnaCJ9LCJhdXRob3JpdGllcyI6WyJjdXN0b21lciJdfQ.igcj_dWdpUEr8WPG06yWUde5bluJMujTdefg24R0XQji5EVoEIQV4xT3D0xOtJDOgK0qSCcz5qUi3xsxbvTJp1xD9WXWIl8lFQA0cP4znSdBEYE-Nv9mLgUaF7QfBpL9_hZtYmeNfkvWk6PqOtvh2VlJ1-5esJ5bzUA3s1h0B8wGKWQUOW3-kCV40iX9gb4BIJfxVDnSytzHQO5iRblHpnWYvJJtWJp2Xx91q22xnvQpBqKaF4n3obYae686apMVMpTFgoFwqcNBaBwStImyh9c_kZMZ8ns-eHFcuzmU_ZA9_VNSK2X6vFcG54H3N3Enf8Dz3RX7LM-Q9iziA82COA")
                .build();
    }
}
