package io.mkrzywanski.pn.subscription.app.api

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class SubscriptionItemSpec extends Specification {
    def "equals and hashcode test"() {
        expect:
        EqualsVerifier.simple().forClass(SubscriptionItem).verify()
    }
}
