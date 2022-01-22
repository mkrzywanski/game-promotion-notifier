package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.api.OfferData
import io.mkrzywanski.pn.email.api.PostData
import org.assertj.core.util.Lists

final class PostDataObjectMother {
    private List<OfferData> offers = List.of(OfferDataObjectMother.offerData().build())
    private String link = "http://post.com"

    static PostDataObjectMother postData() {
        new PostDataObjectMother()
    }

    PostDataObjectMother offers(final List<OfferData> offerData) {
        this.offers = offerData
        this
    }

    PostDataObjectMother link(final String link) {
        this.link = link
        this
    }

    static PostData postDataWithEmptyOffers() {
        new PostData("http://post.com", List.of())
    }


    static PostData postDataWithNullOffers() {
        new PostData("http://post.com", null)
    }

    PostData build() {
        new PostData(link, offers)
    }
}
