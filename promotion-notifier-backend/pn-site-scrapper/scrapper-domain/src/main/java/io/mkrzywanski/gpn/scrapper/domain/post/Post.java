package io.mkrzywanski.gpn.scrapper.domain.post;

import lombok.Value;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Value
public class Post {

    PostId postId;
    Hash hash;
    String source;
    Collection<GameOffer> gameOffers;
    ZonedDateTime datePosted;

    @Override
    public String toString() {
        return "Post{" +
                "source='" + source + '\'' +
                ", gameOffers=" + gameOffers +
                '}';
    }

    public boolean isYoungerThan(final int days, final Clock clock) {
        return ChronoUnit.DAYS.between(datePosted, ZonedDateTime.now(clock)) < days;
    }

}
