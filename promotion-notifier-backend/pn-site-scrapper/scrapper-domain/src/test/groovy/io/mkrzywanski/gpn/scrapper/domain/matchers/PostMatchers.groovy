package io.mkrzywanski.gpn.scrapper.domain.matchers

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer
import io.mkrzywanski.gpn.scrapper.domain.post.Post
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.hamcrest.core.Every

import static io.mkrzywanski.gpn.scrapper.domain.matchers.GameOfferMatchers.isValidGameOffer
import static org.hamcrest.Matchers.*

class PostMatchers {
    static Matcher<Iterable<? extends Post>> containsValidPosts() {
        Every.everyItem(isValidPost())
    }

    static Matcher<Post> isValidPost() {
        AllOf.allOf(
                hasProperty("hash", notNullValue()),
                hasProperty("source", is(not(emptyString()))),
                hasProperty("gameOffers", Every.<GameOffer> everyItem(isValidGameOffer())),
                hasProperty("datePosted", notNullValue())
        )
    }
}
