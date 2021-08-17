package io.mkrzywanski.pn.user.app.contract


import io.mkrzywanski.pn.user.app.api.UserDetailsResponse
import io.mkrzywanski.pn.user.app.domain.UserEndpoint
import io.mkrzywanski.pn.user.app.domain.UserEndpointControllerAdvice
import io.mkrzywanski.pn.user.app.domain.UserFacade
import io.mkrzywanski.pn.user.app.infra.JacksonConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import spock.lang.Specification

import java.text.DateFormat

abstract class BaseClass extends Specification {

    private static final UUID userId = UUID.fromString('e083123c-eac4-463d-bc59-7f2e3fa3cbe1')
    private final UserFacade userFacade = Stub(UserFacade)

    void setup() {
        userFacade.get(userId) >> fakeUserDetails()
        def setup = createSetup()
        RestAssuredMockMvc.standaloneSetup(setup)
    }

    private StandaloneMockMvcBuilder createSetup() {
        JacksonConfig jacksonConfig = new JacksonConfig()
        def format = jacksonConfig.jackson2ObjectMapperBuilder().build().getSerializationConfig().getDateFormat()
        println("aaa")
        println(format)
        def converter = new MappingJackson2HttpMessageConverter(jacksonConfig.jackson2ObjectMapperBuilder().build())
        def setup = MockMvcBuilders.standaloneSetup(new UserEndpoint(userFacade))
        setup.controllerAdvice = [UserEndpointControllerAdvice]
        setup.messageConverters = [converter]
        setup
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
