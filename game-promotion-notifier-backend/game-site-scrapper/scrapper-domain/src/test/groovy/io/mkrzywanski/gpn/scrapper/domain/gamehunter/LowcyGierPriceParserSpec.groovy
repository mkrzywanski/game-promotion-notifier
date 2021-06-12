package io.mkrzywanski.gpn.scrapper.domain.gamehunter

import io.mkrzywanski.gpn.scrapper.domain.post.CompositeGamePrice
import io.mkrzywanski.gpn.scrapper.domain.post.Currencies
import io.mkrzywanski.gpn.scrapper.domain.post.EmptyGamePrice
import io.mkrzywanski.gpn.scrapper.domain.post.NumberGamePrice
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.aMapWithSize
import static org.hamcrest.collection.IsMapContaining.hasEntry
import static org.hamcrest.core.AllOf.allOf
import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.core.IsInstanceOf.instanceOf

class LowcyGierPriceParserSpec extends Specification {

    def "should parse price composite game price"() {
        given:
        def input = "20 zł, 30 €"

        when:
        def gamePrice = LowcyGierPriceParser.parse(input)

        then:
        gamePrice instanceOf(CompositeGamePrice)
        def compositeGamePrice = gamePrice as CompositeGamePrice
        assertThat compositeGamePrice.asString(), equalTo("30 EUR, 20 PLN")
        assertThat compositeGamePrice.asMap(), allOf(
                hasEntry(Currencies.PLN, BigDecimal.valueOf(20)),
                hasEntry(Currencies.EUR, BigDecimal.valueOf(30))
        )
    }

    def "should parse empty game price"() {
        when:
        def gamePrice = LowcyGierPriceParser.parse(input)

        then:
        gamePrice instanceOf(EmptyGamePrice)
        def emptyGamePrice = gamePrice as EmptyGamePrice
        assertThat emptyGamePrice.asString(), equalTo("not available")
        assertThat emptyGamePrice.asMap(), aMapWithSize(0)

        where:
        input              | _
        ""                 | _
        "no prices string" | _
    }

    def "should parse single price"() {
        when:
        def gamePrice = LowcyGierPriceParser.parse(input)

        then:
        gamePrice instanceOf(NumberGamePrice)
        def numberGamePrice = gamePrice as NumberGamePrice
        assertThat numberGamePrice.asString(), equalTo(output)
        assertThat numberGamePrice.asMap(), allOf(hasEntry(currency, value), aMapWithSize(1))

        where:
        input   | output   | currency       | value
        "20 zł" | "20 PLN" | Currencies.PLN | BigDecimal.valueOf(20)
        "20 €"  | "20 EUR" | Currencies.EUR | BigDecimal.valueOf(20)

    }
}
