package io.mkrzywanski.gpn.email.data

import io.mkrzywanski.gpn.email.api.OfferData
import io.mkrzywanski.gpn.email.domain.Price

import java.util.Set

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
