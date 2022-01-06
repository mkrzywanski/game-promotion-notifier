package io.mkrzywanski.pn.matching.subscription;

public interface SubscriptionServiceClient {
    MatchingResponse match(MatchingRequest matchingRequest);
}
