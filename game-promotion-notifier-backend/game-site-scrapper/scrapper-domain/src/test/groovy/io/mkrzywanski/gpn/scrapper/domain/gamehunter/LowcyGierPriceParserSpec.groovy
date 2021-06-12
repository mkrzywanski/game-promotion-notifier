package io.mkrzywanski.gpn.scrapper.domain.gamehunter

import io.mkrzywanski.gpn.scrapper.domain.post.CompositeGamePrice
import io.mkrzywanski.gpn.scrapper.domain.post.Currencies
import io.mkrzywanski.gpn.scrapper.domain.post.GamePrice
import io.mkrzywanski.gpn.scrapper.domain.post.NumberGamePrice
import org.hamcrest.collection.IsMapContaining
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsInstanceOf
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.collection.IsMapContaining.*
import static org.hamcrest.core.AllOf.allOf
import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.core.IsInstanceOf.*

class LowcyGierPriceParserSpec extends Specification {

    def "should parse price"() {
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
}
