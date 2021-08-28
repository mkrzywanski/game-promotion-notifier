package io.mkrzywanski.pn.matching.subscription;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpSubscriptionServiceClient implements SubscriptionServiceClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public HttpSubscriptionServiceClient(@Value("${gpn.subscription-service.url}") final String baseUrl, final RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public MatchingResponse match(final MatchingRequest matchingRequest) {
        return restTemplate.postForObject(baseUrl + "/v1/subscriptions/match", matchingRequest, MatchingResponse.class);
    }
}
