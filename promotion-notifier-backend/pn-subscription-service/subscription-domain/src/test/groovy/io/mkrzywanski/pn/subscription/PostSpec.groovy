package io.mkrzywanski.pn.subscription

import nl.jqno.equalsverifier.EqualsVerifier
import spock.lang.Specification

class PostSpec extends Specification {
    def "equals and hashcode test"() {
        expect:
        EqualsVerifier.simple().forClass(Post).verify()
    }
}
