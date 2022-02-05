package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;

@Slf4j
class NotTooOldFilter implements PostFilter {

    private final int minimalDaysInterval;
    private final Clock clock;

    NotTooOldFilter(final int minimalDaysInterval, final Clock clock) {
        this.minimalDaysInterval = minimalDaysInterval;
        this.clock = clock;
    }

    @Override
    public String name() {
        return "NotTooOldFilter";
    }

    @Override
    public boolean test(final Post post) {
        return post.isYoungerThan(minimalDaysInterval, clock);
    }
}
