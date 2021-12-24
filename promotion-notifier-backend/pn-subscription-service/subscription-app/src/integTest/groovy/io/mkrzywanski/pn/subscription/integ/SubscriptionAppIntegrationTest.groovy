package io.mkrzywanski.pn.subscription.integ

import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.pn.subscription.MatchingRequest
import io.mkrzywanski.pn.subscription.Offer
import io.mkrzywanski.pn.subscription.Post
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionElasticModel
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest
import io.mkrzywanski.pn.subscription.app.api.SubscriptionItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.data.elasticsearch.core.query.StringQuery
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import org.springframework.security.oauth2.client.registration.*

import java.util.function.Function

import static org.hamcrest.Matchers.notNullValue
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = IntegrationTestConfig)
@AutoConfigureMockMvc
class SubscriptionAppIntegrationTest extends Specification {

    @Autowired
    ElasticsearchOperations elasticsearchOperations

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    KeyCloakAccess keyCloakAccess

    private final def postId = UUID.fromString("b5e6a983-d94a-400e-9672-4560657838e0")
    private final def userId = UUID.fromString("e48fde73-4eca-499b-b649-8263ada90995")
    private final def offerId = UUID.fromString('fb1fdfc4-6dfe-4e52-b41c-67d63274ec25')

    void cleanup() {
        def query = new StringQuery("{\"match_all\": {}}")
        elasticsearchOperations.delete(query, SubscriptionElasticModel, IndexCoordinates.of("subscriptions"))
    }

    def "should match new post offers to existing subscriptions"() {
        given:
        def token = keyCloakAccess.getUserToken()
        existingDocumentIsSaved()
        def matchingRequest = post()
        def content = objectMapper.writeValueAsString(matchingRequest)

        when: "request is preformed"
        def response = mockMvc.perform(post("/v1/subscriptions/match", content).header("Authorization", "Bearer ${token}"))

        then:
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
               {
                    "matches": [
                        {
                            "userId": "${userId}",
                            "postId": "${postId}",
                            "offerId": "${offerId}"
                        }
                    ]
                }""", false))
    }

    def "should persist subscription"() {
        given:
        def token = keyCloakAccess.getUserToken()
        def subscriptionRequest = new CreateSubscriptionRequest(userId, Set.of("Rainbow Six"))
        def requestJson = objectMapper.writeValueAsString(subscriptionRequest)


        def request = post("/v1/subscriptions", requestJson).header("Authorization", "Bearer ${token}")
        when:
        def response = mockMvc.perform(request)

        then:
        response.andExpect(status().isCreated())
            .andExpect(jsonPath('$.subscriptionId', notNullValue()))
    }

    private static MockHttpServletRequestBuilder post(String url, String content) {
        request(MockMvcRequestBuilders::post, url, content)
    }

    private static MockHttpServletRequestBuilder get(String url, String content) {
        request(MockMvcRequestBuilders::get, url, content)
    }

    private static MockHttpServletRequestBuilder request(Function<String, MockHttpServletRequestBuilder> f, String url, String content) {
        return f.apply(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
    }

    private MatchingRequest post() {
        def post = new Post(postId, List.of(new Offer(offerId, "rainbow")))
        def postsToMatch = Set.of(post)
        new MatchingRequest(postsToMatch)
    }

    private SubscriptionElasticModel existingDocumentIsSaved() {
        def subscriptionId = UUID.randomUUID()
        def subscriptionElasticModel = new SubscriptionElasticModel(userId, Set.of("rainbow"))
        def query = new IndexQueryBuilder().withId(subscriptionId.toString()).withObject(subscriptionElasticModel).build()
        elasticsearchOperations.index(query, IndexCoordinates.of("subscriptions"))
        subscriptionElasticModel
    }
}
