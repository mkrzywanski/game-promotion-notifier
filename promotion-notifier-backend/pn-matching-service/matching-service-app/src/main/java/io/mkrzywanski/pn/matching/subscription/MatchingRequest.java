package io.mkrzywanski.pn.matching.subscription;

import lombok.Value;

import java.util.Set;

@Value
public class MatchingRequest {
    Set<PostData> postsToMatch;
}
