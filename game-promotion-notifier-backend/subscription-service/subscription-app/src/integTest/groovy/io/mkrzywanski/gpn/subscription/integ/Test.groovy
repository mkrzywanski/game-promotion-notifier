package io.mkrzywanski.gpn.subscription.integ

import com.fasterxml.jackson.databind.ObjectMapper
import io.mkrzywanski.gpn.subscription.MatchingRequest
import io.mkrzywanski.gpn.subscription.Offer
import io.mkrzywanski.gpn.subscription.Post
import io.mkrzywanski.gpn.subscription.app.adapters.SubscriptionElasticModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.IndexQuery
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = IntegTestConfig)
@AutoConfigureMockMvc
class Test extends Specification {

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    ElasticsearchOperations elasticsearchOperations

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    private final def postId = UUID.fromString("b5e6a983-d94a-400e-9672-4560657838e0")
    private final def userId = UUID.fromString("e48fde73-4eca-499b-b649-8263ada90995")
    private final int offerId = 1

    def "sfsfsd"() {
        given:
        def existingDocument = existingDocumentIsSaved()
        def matchingRequest = request()
        def content = objectMapper.writeValueAsString(matchingRequest)

        when: ""
        def response = mockMvc.perform(request("/v1/subscriptions/match", content))

        then:
        applicationContext != null
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
               {
                    "matches": [
                        {
                            "userId": "${userId}",
                            "postId": "${postId}",
                            "offerId": ${offerId}
                        }
                    ]
                }""", false))
    }

    private static MockHttpServletRequestBuilder request(String url, String content) {
        MockMvcRequestBuilders.post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
    }

    private MatchingRequest request() {
        def post = new Post(postId, List.of(new Offer("rainbow", offerId)))
        def postsToMatch = Set.of(post)
        new MatchingRequest(postsToMatch)
    }

    private SubscriptionElasticModel existingDocumentIsSaved() {
        UUID subscriptionId = UUID.randomUUID()
        def subscriptionElasticModel = new SubscriptionElasticModel(userId, Set.of("rainbow"))
        def query = new IndexQueryBuilder().withId(subscriptionId.toString()).withObject(subscriptionElasticModel).build()
        elasticsearchOperations.index(query, IndexCoordinates.of("subscriptions"))
        elasticsearchOperations
        subscriptionElasticModel
    }
}
