package io.mkrzywanski.pn.user.app

import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.pn.user.app.adapters.persistance.JpaUserRepository
import io.mkrzywanski.pn.user.app.adapters.persistance.UserEntity
import io.mkrzywanski.pn.user.app.api.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.is
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = [IntegrationTestConfig])
@AutoConfigureMockMvc
class UserAppIntegrationTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    JpaUserRepository jpaUserRepository

    void cleanup() {
        jpaUserRepository.deleteAll()
    }

    def "should create user"() {
        given: "request"
        def createUserRequest = new CreateUserRequest("name", "username", "email@google.com")
        def request = post("/v1/users")
                .content(json(createUserRequest))
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)

        when: "request is sent"
        def response = mockMvc.perform(request)

        then:
        response.andExpect(status().isCreated())
        userIsCreated(createUserRequest)
    }

    def "should get user details"() {
        given:
        def userEntity = aUser()

        and:
        jpaUserRepository.save(userEntity)

        and:
        def request = get("/v1/users/${userEntity.getUniqueId()}")

        when:
        def response = mockMvc.perform(request)

        then:
        response.andExpect(jsonPath('$.userId', is(equalTo(userEntity.uniqueId.toString()))))
                .andExpect(jsonPath('$.username', is(equalTo(userEntity.userName))))
                .andExpect(jsonPath('$.firstName', is(equalTo(userEntity.firstName))))
                .andExpect(jsonPath('$.email', is(equalTo(userEntity.email))))
    }

    static def aUser() {
        return new UserEntity(UUID.randomUUID(), "user", "firstName", "email@google.com")
    }

    private String json(final Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    def userIsCreated(CreateUserRequest createUserRequest) {
        def user = jpaUserRepository.findAll().first()
        user.email == createUserRequest.email
        user.firstName == createUserRequest.firstName
        user.userName == createUserRequest.userName
        user.uniqueId != null
    }
}
