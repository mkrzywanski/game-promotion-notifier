package io.mkrzywanski.pn.matching.subscription;

import io.mkrzywanski.pn.matching.subscription.api.MatchingRequest;
import io.mkrzywanski.pn.matching.subscription.api.MatchingResponse;

public interface SubscriptionServiceClient {
    MatchingResponse match(MatchingRequest matchingRequest);
}
