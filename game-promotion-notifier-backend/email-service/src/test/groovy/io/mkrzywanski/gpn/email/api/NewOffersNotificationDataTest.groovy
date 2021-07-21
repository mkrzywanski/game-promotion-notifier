package io.mkrzywanski.gpn.email.api

import io.mkrzywanski.gpn.email.data.OfferDataObjectMother
import org.assertj.core.util.Lists
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.Validator

import static io.mkrzywanski.gpn.email.data.NewOffersNotificationDataObjectMother.*
import static io.mkrzywanski.gpn.email.data.PostDataObjectMother.*
import static io.mkrzywanski.gpn.email.data.UserDataObjectMother.*

class NewOffersNotificationDataTest extends Specification {
    Validator validator = Validation.buildDefaultValidatorFactory().validator

    @Unroll
    def "should fail when data is invalid"() {
        given:
        newOffersNotificationData
        when:
        def violations = validator.validate(newOffersNotificationData)
        then:
        !violations.isEmpty()

        where:
        newOffersNotificationData                                                                   | _
        newOffersNotificationData().userData(withUsername(null)).build()                            | _
        newOffersNotificationData().userData(withUsername("")).build()                              | _
        newOffersNotificationData().userData(withName(null)).build()                                | _
        newOffersNotificationData().userData(withName("")).build()                                  | _
        newOffersNotificationData().email("").build()                                               | _
        newOffersNotificationData().email(null).build()                                             | _
        newOffersNotificationData().email("invalid email").build()                                  | _
        newOffersNotificationData().postData(null).build()                                          | _
        newOffersNotificationData().postData(Lists.emptyList()).build()                             | _
        newOffersNotificationData().postData(List.of(postDataWithEmptyOffers())).build()            | _
        newOffersNotificationData().postData(List.of(postDataWithNullOffers())).build()             | _
        newOffersNotificationData().postData(List.of(postData().offers(null).build())).build()      | _
        newOffersNotificationData().postData(List.of(postData().offers(List.of()).build())).build() | _
    }

}
