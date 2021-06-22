import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.gpn.user.app.adapters.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@SpringBootTest(classes = [IntegrationTestConfig])
@AutoConfigureMockMvc
class Test extends Specification{

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    ApplicationContext applicationContext

    def "app"() {
        given: "aa"
        CreateUserRequest createUserRequest = new CreateUserRequest("name", "username", "email")

        def request = MockMvcRequestBuilders.post("/v1/users")
                .content(json(createUserRequest))
                .contentType(APPLICATION_JSON_VALUE)
        .accept(APPLICATION_JSON_VALUE)
        when:
        def perform = mockMvc.perform(request)
        then:
        applicationContext != null
        println "aaaaaa" + perform.andReturn().response.contentAsString
        perform.andExpect(status().isCreated())
    }

    private String json(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }
}
