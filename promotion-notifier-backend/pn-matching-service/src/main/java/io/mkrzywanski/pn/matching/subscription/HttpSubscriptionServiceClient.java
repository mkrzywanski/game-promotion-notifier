package io.mkrzywanski.pn.matching.subscription;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HttpSubscriptionServiceClient implements SubscriptionServiceClient {

    private final String baseUrl;
    private final WebClient webclient;

    public HttpSubscriptionServiceClient(@Value("${gpn.subscription-service.url}") final String baseUrl, final WebClient webclient) {
        this.baseUrl = baseUrl;
        this.webclient = webclient;
    }

    @Override
    public MatchingResponse match(final MatchingRequest matchingRequest) {
        return webclient.post()
                .uri(baseUrl + "/v1/subscriptions/match")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(matchingRequest)
                .retrieve()
                .bodyToMono(MatchingResponse.class)
                .block();
    }
}
