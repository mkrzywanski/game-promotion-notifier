package io.mkrzywanski.pn.user.app.contract


import io.mkrzywanski.pn.user.app.api.UserDetailsResponse
import io.mkrzywanski.pn.user.app.domain.UserEndpoint
import io.mkrzywanski.pn.user.app.domain.UserFacade
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

abstract class BaseClass extends Specification {

    private static final UUID userId = UUID.fromString('e083123c-eac4-463d-bc59-7f2e3fa3cbe1')
    UserFacade userFacade = Stub(UserFacade)

    void setup() {
        userFacade.get(userId) >> fakeUserDetails()
        def setup = MockMvcBuilders.standaloneSetup(new UserEndpoint(userFacade))
        RestAssuredMockMvc.standaloneSetup(setup)
    }

    private static Optional<UserDetailsResponse> fakeUserDetails() {
        def userDetailsResponse = UserDetailsResponse.builder()
                .userId(userId)
                .username('test')
                .firstName('test')
                .email('test@test.pl')
                .build()
        Optional.of(userDetailsResponse)
    }
}
