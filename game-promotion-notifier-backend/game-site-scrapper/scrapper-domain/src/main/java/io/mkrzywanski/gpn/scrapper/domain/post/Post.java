package io.mkrzywanski.gpn.scrapper.domain.post;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class Post {

    private final Hash hash;
    private final String source;
    private final Collection<GameOffer> gameOffers;
    private final ZonedDateTime datePosted;

    public Post(final Hash hash, final String source, final Collection<GameOffer> gameOffers, final ZonedDateTime datePosted) {
        this.hash = hash;
        this.source = source;
        this.gameOffers = gameOffers;
        this.datePosted = datePosted;
    }

    @Override
    public String toString() {
        return "Post{" +
                "source='" + source + '\'' +
                ", gameOffers=" + gameOffers +
                '}';
    }

    public Hash getHash() {
        return hash;
    }

    public String getSource() {
        return source;
    }

    public Collection<GameOffer> getGameOffers() {
        return gameOffers;
    }

    public ZonedDateTime getDatePosted() {
        return datePosted;
    }

    public boolean isYoungerThan(final int days, final Clock clock) {
        return ChronoUnit.DAYS.between(datePosted, ZonedDateTime.now(clock)) < days;
    }
}
