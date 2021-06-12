package io.mkrzywanski.gpn.scrapper.domain.post;

import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostObjectMother {

    private static final int MONTH = 12;
    private static final int DAY_OF_MONTH = 11;
    private static final int HOUR = 10;
    private static final int MINUTE = 10;
    private static final int YEAR = 2020;

    private String source;
    private Hash hash;
    private Collection<GameOffer> gameOffers;
    private ZonedDateTime datePosted;

    private PostObjectMother() {
        this.source = "index";
        this.hash = Hash.compute(source);
        this.datePosted = ZonedDateTime.of(LocalDate.of(YEAR, MONTH, DAY_OF_MONTH), LocalTime.of(HOUR, MINUTE), ZoneId.systemDefault());
        this.gameOffers = new ArrayList<>(List.of(GameOfferObjectMother.newInstance().build()));
    }

    public static PostObjectMother newInstance() {
        return new PostObjectMother();
    }

    public PostObjectMother withSource(final String source) {
        this.source = source;
        this.hash = Hash.compute(source);
        return this;
    }

    public PostObjectMother withDatePosted(final ZonedDateTime datePosted) {
        this.datePosted = datePosted;
        return this;
    }

    public Post build() {
        return new Post(hash, source, gameOffers, datePosted);
    }
}
