package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.domain.Price
import io.mkrzywanski.pn.email.api.OfferData

final class OfferDataObjectMother {

    private String name = "Rainbow Six siege"
    private Set<Price> prices = Set.of(PriceObjectMother.price().build())
    private String url = "http://test.com"

    private OfferDataObjectMother() {
    }

    static OfferDataObjectMother offerData() {
        new OfferDataObjectMother()
    }

    OfferDataObjectMother name(String name) {
        this.name = name
        this
    }

    OfferDataObjectMother prices(Set<Price> prices) {
        this.prices = prices
        this
    }

    OfferDataObjectMother url(String url) {
        this.url = url;
        this
    }

    OfferData build() {
        return new OfferData(name, url, prices)
    }
}
