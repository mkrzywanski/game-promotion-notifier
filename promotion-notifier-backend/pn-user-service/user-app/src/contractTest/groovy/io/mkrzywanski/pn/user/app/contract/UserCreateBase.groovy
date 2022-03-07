package io.mkrzywanski.pn.user.app.contract

import io.mkrzywanski.pn.user.app.api.CreateUserRequest
import io.mkrzywanski.pn.user.app.api.UserCreatedResponse
import io.mkrzywanski.pn.user.app.domain.UserEndpoint
import io.mkrzywanski.pn.user.app.domain.UserEndpointControllerAdvice
import io.mkrzywanski.pn.user.app.domain.UserFacade
import io.mkrzywanski.pn.user.app.infra.JacksonConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import spock.lang.Specification

abstract class UserCreateBase extends Specification {

    private static final UUID userId = UUID.fromString('db3ca7f3-7c8b-48ab-8245-4126a1389daf')
    private final UserFacade userFacade = Stub(UserFacade)

    void setup() {
        userFacade.create(new CreateUserRequest(userId, "Michal", "K","user1", "test@test.pl", )) >> new UserCreatedResponse(userId.toString())
        def setup = createSetup()
        RestAssuredMockMvc.standaloneSetup(setup)
    }

    private StandaloneMockMvcBuilder createSetup() {
        final def jacksonConfig = new JacksonConfig()
        def converter = new MappingJackson2HttpMessageConverter(jacksonConfig.jackson2ObjectMapperBuilder().build())
        def setup = MockMvcBuilders.standaloneSetup(new UserEndpoint(userFacade))
        setup.controllerAdvice = [UserEndpointControllerAdvice]
        setup.messageConverters = [converter]
        setup
    }
}
