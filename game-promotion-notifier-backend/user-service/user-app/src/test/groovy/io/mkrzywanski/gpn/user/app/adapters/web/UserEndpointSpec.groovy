package io.mkrzywanski.gpn.user.app.adapters.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.gpn.user.app.adapters.web.UserFacade
import io.mkrzywanski.gpn.user.app.api.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

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
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())

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

}
