package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.api.OfferData
import io.mkrzywanski.pn.email.api.PostData
import org.assertj.core.util.Lists

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
