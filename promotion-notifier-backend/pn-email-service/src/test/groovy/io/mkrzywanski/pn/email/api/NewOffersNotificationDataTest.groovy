package io.mkrzywanski.pn.email.api


import spock.lang.Specification
import spock.lang.Unroll

import jakarta.validation.Validation
import jakarta.validation.Validator

import static io.mkrzywanski.pn.email.data.NewOffersNotificationDataObjectMother.*
import static io.mkrzywanski.pn.email.data.PostDataObjectMother.*
import static io.mkrzywanski.pn.email.data.UserDataObjectMother.*
import static java.util.Collections.emptyList

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
        newOffersNotificationData                                                                    | _
        newOffersNotificationData().userData(withUsername(null)).build()                             | _
        newOffersNotificationData().userData(withUsername("")).build()                               | _
        newOffersNotificationData().userData(userData().id(null).build()).build()                    | _
        newOffersNotificationData().userData(withFirstName(null)).build()                            | _
        newOffersNotificationData().userData(withFirstName("")).build()                              | _
        newOffersNotificationData().userData(withEmail("")).build()                                  | _
        newOffersNotificationData().userData(withEmail(null)).build()                                | _
        newOffersNotificationData().userData(withEmail("invalid email")).build()                     | _
        newOffersNotificationData().postData(null).build()                                           | _
        newOffersNotificationData().postData(emptyList()).build()                                    | _
        newOffersNotificationData().postData(List.of(postDataWithEmptyOffers())).build()             | _
        newOffersNotificationData().postData(List.of(postDataWithNullOffers())).build()              | _
        newOffersNotificationData().postData(List.of(postData().link("").build())).build()           | _
        newOffersNotificationData().postData(List.of(postData().link(null).build())).build()         | _
        newOffersNotificationData().postData(List.of(postData().link("wrong link").build())).build() | _
        newOffersNotificationData().postData(List.of(postData().offers(null).build())).build()       | _
        newOffersNotificationData().postData(List.of(postData().offers(List.of()).build())).build()  | _
    }

}
