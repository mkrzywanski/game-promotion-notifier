package io.mkrzywanski.pn.matching.subscription.api;

import lombok.Value;

import java.util.Set;

@Value
public class MatchingRequest {
    Set<PostData> postsToMatch;
}
