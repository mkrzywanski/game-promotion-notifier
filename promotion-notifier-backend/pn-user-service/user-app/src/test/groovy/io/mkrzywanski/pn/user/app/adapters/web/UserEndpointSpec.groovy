package io.mkrzywanski.pn.user.app.adapters.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.pn.user.app.api.CreateUserRequest
import io.mkrzywanski.pn.user.app.domain.UserFacade
import io.mkrzywanski.pn.user.app.infra.UserServiceConstants
import org.hamcrest.Matchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

import static io.mkrzywanski.pn.user.app.infra.UserServiceConstants.*
import static org.hamcrest.Matchers.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class UserEndpointSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockBean
    UserFacade userFacade

    @Autowired
    ObjectMapper objectMapper

    @Unroll
    def "should reject invalid new user creation request"() {
        given: "request"
        def createUserRequest = new CreateUserRequest(firstName, userName, email)
        def request = MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json(createUserRequest))
        when:
        def response = mockMvc.perform(request)

        then:
        response.andExpect(status().isBadRequest())

        where:
        firstName | userName | email
        "Jhon"    | "Jonny"  | null
        "Jhon"    | null     | "jhon@gmail.com"
        null      | "Jonny"  | "jhon@gmail.com"
        "Jhon"    | "Jonny"  | ""
        "Jhon"    | ""       | "jhon@gmail.com"
        ""        | "Jonny"  | "jhon@gmail.com"
    }

    def json(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    def "should return 404"() {
        given:
        Mockito.when(userFacade.get(Mockito.any())).thenReturn(Optional.empty())
        def userId = '2df3a813-5e1b-4292-8978-ec4bdf6fc942'
        def request = MockMvcRequestBuilders.get("/v1/users/${userId}")

        when:
        def response = mockMvc.perform(request)
        then:
        response.andExpect(status().isNotFound())
            .andExpect(jsonPath('$.timestamp', notNullValue()))
            .andExpect(jsonPath('$.message', containsString("User with id ${userId} not found")))
            .andExpect(jsonPath('$.status', equalTo(NOT_FOUND.value())))
            .andExpect(jsonPath('$.serviceName', equalTo(USER_SERVICE)))
            .andExpect(jsonPath('$.path', notNullValue()))
    }
}
