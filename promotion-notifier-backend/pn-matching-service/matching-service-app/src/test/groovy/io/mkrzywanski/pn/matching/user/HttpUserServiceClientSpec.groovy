package io.mkrzywanski.pn.matching.user

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class HttpUserServiceClientSpec extends Specification {

    def url = "http://user-service"
    def restTemplateMock = Stub(RestTemplate)
    def client = new HttpUserServiceClient(url, restTemplateMock)
    def userId = UUID.randomUUID()
    def userDetails = new UserDetails(userId, "testUsername", "testFirstName", "test@test.com")

    def "should retrive user details"() {
        given:
        userServiceReturnsUserDetails()
        when:
        def actualUserDetails = client.getUserDetails(userId)
        then:
        userDetails == actualUserDetails
    }

    private void userServiceReturnsUserDetails() {
        restTemplateMock.getForEntity("${url}/v1/users/${userId}", UserDetails.class) >> ResponseEntity.ok(userDetails)
    }
}