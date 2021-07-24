package io.mkrzywanski.pn.subscription;

import java.util.Set;

public class MatchingRequest {
    private Set<Post> postsToMatch;

    private MatchingRequest() {
    }

    public MatchingRequest(final Set<Post> postsToMatch) {
        this.postsToMatch = postsToMatch;
    }

    public Set<Post> getPostsToMatch() {
        return postsToMatch;
    }
}
