package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.domain.Price
import io.mkrzywanski.pn.email.api.OfferData

final class OfferDataObjectMother {

    private String name = "Rainbow Six siege"
    private Set<Price> prices = Set.of(PriceObjectMother.price().build())

    private OfferDataObjectMother() {
    }

    static OfferDataObjectMother offerData() {
        return new OfferDataObjectMother()
    }

    OfferDataObjectMother name(String name) {
        this.name = name
        return this
    }

    OfferDataObjectMother prices(Set<Price> prices) {
        this.prices = prices
        return this
    }

    OfferData build() {
        return new OfferData(name, prices)
    }
}
