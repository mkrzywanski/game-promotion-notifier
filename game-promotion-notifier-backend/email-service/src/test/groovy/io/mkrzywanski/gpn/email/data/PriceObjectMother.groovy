package io.mkrzywanski.gpn.email.data

import io.mkrzywanski.gpn.email.domain.Currencies
import io.mkrzywanski.gpn.email.domain.Price

import java.math.BigDecimal
import java.util.Currency

final class PriceObjectMother {
    private Currency currency = Currencies.PLN
    private BigDecimal value = BigDecimal.valueOf(20)

    PriceObjectMother() {
    }

    static PriceObjectMother price() {
        return new PriceObjectMother()
    }

    Price build() {
        return Price.of(currency, value)
    }

    PriceObjectMother value(BigDecimal bigDecimal) {
        this.value = bigDecimal
        return this
    }


    PriceObjectMother currency(Currency currency) {
        this.currency = currency
        return this
    }


}
