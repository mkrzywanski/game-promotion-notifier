package io.mkrzywanski.gpn.scrapper.app.integ

import com.github.tomakehurst.wiremock.WireMockServer
import io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts.GameOfferModel
import io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts.PostModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.responseDefinition
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

import java.time.ZonedDateTime

@SpringBootTest(classes = [IntegrationTestConfig])
class GameHunterScrapperIntegrationSpec extends Specification {

    @Autowired
    WireMockServer wireMockServer

    @Autowired
    MongoTemplate mongoTemplate

    def "should scrap game hunter page"() {
        given: "Game hunter server is running and scrapper is scrapping"
        prepareAlreadySavedPost()
        prepareServerStub()
        Thread.sleep(2000)

        when: "scrapper is invoked"

        then:
        def savedPosts = mongoTemplate.findAll(PostModel)
        savedPosts.size() == 20
    }

    private def prepareAlreadySavedPost() {
        List<GameOfferModel> of = List.of()
        PostModel postModel = new PostModel("Lj7tmcaPPsUW0BuSwWWkdLYnh6z045XL8KR5PcjtzhQ=", "game-hunter", of, ZonedDateTime.parse("2021-06-05T10:35Z").toInstant())
        mongoTemplate.save(postModel)
    }

    private def prepareServerStub() {
        def html = Files.readString(Paths.get("src/integTest/resources/gamehunter/index.html"))
        def response = responseDefinition().withBody(html).withStatus(200)
        wireMockServer.stubFor(get(urlEqualTo("/page/1/")).willReturn(response))
    }
}
