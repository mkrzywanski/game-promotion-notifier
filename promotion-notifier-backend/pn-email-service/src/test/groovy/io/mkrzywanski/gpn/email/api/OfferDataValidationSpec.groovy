package io.mkrzywanski.gpn.email.api

import io.mkrzywanski.gpn.email.data.OfferDataObjectMother
import io.mkrzywanski.gpn.email.data.PriceObjectMother
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator

import static io.mkrzywanski.gpn.email.data.OfferDataObjectMother.*

class OfferDataValidationSpec extends Specification {
    Validator validator = Validation.buildDefaultValidatorFactory().validator

    @Unroll
    def "should validate offer data"() {
        when:
        def violations = validator.validate(offerData)

        then:
        !violations.isEmpty()

        where:
        offerData                                                                            | _
        offerData().name("").build()                                                         | _
        offerData().name(null).build()                                                       | _
        offerData().prices(null).build()                                                     | _
        offerData().prices(Collections.emptySet()).build()                                   | _
        offerData().prices(Set.of(PriceObjectMother.price().currency(null).build())).build() | _
        offerData().prices(Set.of(PriceObjectMother.price().value(null).build())).build()    | _

    }
}
