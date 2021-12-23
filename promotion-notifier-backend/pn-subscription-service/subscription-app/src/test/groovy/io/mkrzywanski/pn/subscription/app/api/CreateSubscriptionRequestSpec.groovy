package io.mkrzywanski.pn.subscription.app.api

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class CreateSubscriptionRequestSpec extends Specification {
    def "equals and hashcode"() {
        expect:
        EqualsVerifier.simple().forClass(CreateSubscriptionRequest).verify()
    }
}
