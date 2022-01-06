package io.mkrzywanski.gpn.scrapper.domain.matchers

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

import static org.hamcrest.Matchers.*

class GameOfferMatchers {
    static Matcher<GameOffer> isValidGameOffer() {
        AllOf.allOf(
                hasProperty("id", is(notNullValue())),
                hasProperty("gameName", is(not(emptyString()))),
                hasProperty("link", is(not(emptyString()))),
                hasProperty("gamePrice", notNullValue())
        )
    }
}
