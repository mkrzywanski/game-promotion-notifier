package io.mkrzywanski.keycloak.listeners

import spock.lang.Specification

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable

class UserServicePropertiesProviderSpec extends Specification {

    def 'should return correct user service url from env'() {
        given:
        def url = "http://localhost"
        def result = withEnvironmentVariable(UserServicePropertiesProvider.USER_SERVICE_URL_ENV, url)
                .execute(() ->
                        UserServicePropertiesProvider.url
                )

        expect:
        result == url
    }

    def "should throw exception when env is not set"() {
        given: "url not set"

        when:
        UserServicePropertiesProvider.url

        then:
        thrown(IllegalStateException)
    }
}
