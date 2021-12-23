package io.mkrzywanski.pn.subscription;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor
public class MatchingRequest {
    Set<Post> postsToMatch;

    private MatchingRequest() {
        this(Set.of());
    }
}
