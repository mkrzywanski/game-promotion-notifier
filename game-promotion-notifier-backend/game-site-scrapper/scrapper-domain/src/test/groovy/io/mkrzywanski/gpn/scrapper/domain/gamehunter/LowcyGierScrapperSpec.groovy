package io.mkrzywanski.gpn.scrapper.domain.gamehunter

import io.mkrzywanski.gpn.scrapper.domain.post.PostObjectMother
import spock.lang.Specification

class LowcyGierScrapperSpec extends Specification {

    def client = Stub(LowcyGierClient)
    def parser = Mock(LowcyGierParser)
    def scrapper = new LowcyGierScrapper(client, parser)

    def "should scrap"() {
        given:
        clientReturnsResponseWhenAskingForPage("index", 1)
        parserReturnsPostsWhenParsingHtml(List.of(PostObjectMother.newInstance().build()), "index")

        when:
        def scrap = scrapper.scrap(1)

        then:
        scrap.size() == 1
    }

    def "should return empty list when scrapping fails"() {
        given:
        clientThrowsException()

        when:
        def scrap = scrapper.scrap(1)

        then:
        scrap.size() == 0
    }

    private void clientThrowsException() {
        client.getPage(_ as Integer) >> {
            throw new LowcyGierClientException()
        }
    }


    private void parserReturnsPostsWhenParsingHtml(def posts, def html) {
        parser.parse(html) >> posts
    }

    private void clientReturnsResponseWhenAskingForPage(def response, def page) {
        client.getPage(page) >> response
    }
}
