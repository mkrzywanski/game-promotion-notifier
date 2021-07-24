package io.mkrzywanski.gpn.email.data

import io.mkrzywanski.gpn.email.api.OfferData
import io.mkrzywanski.gpn.email.api.PostData
import org.assertj.core.util.Lists

import java.util.List

final class PostDataObjectMother {
    private List<OfferData> offers = List.of(OfferDataObjectMother.offerData().build())

    static PostDataObjectMother postData() {
        return new PostDataObjectMother()
    }

    PostDataObjectMother offers(final List<OfferData> offerData) {
        this.offers = offerData
        return this
    }

    static PostData postDataWithEmptyOffers() {
        return new PostData(Lists.emptyList())
    }


    static PostData postDataWithNullOffers() {
        return new PostData(null)
    }

    PostData build() {
        return new PostData(offers)
    }
}
