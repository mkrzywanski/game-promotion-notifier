package io.mkrzywanski.pn.subscription;

import java.util.Set;

public class SubscriptionMatchingResult {
    private final Set<Match> matches;

    public SubscriptionMatchingResult(final Set<Match> matches) {
        this.matches = matches;
    }

    public Set<Match> getMatches() {
        return matches;
    }
}
