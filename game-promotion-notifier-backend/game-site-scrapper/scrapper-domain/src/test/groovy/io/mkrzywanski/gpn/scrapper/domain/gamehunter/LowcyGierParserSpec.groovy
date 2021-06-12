package io.mkrzywanski.gpn.scrapper.domain.gamehunter


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static io.mkrzywanski.gpn.scrapper.domain.matchers.PostMatchers.containsValidPosts
import static org.hamcrest.Matchers.hasSize
import static spock.util.matcher.HamcrestSupport.that

class LowcyGierParserSpec extends Specification {

    LowcyGierParser lowcyGierParser = new LowcyGierParser()

    def "should parse page"() {
        given:
        def html = Files.readString(Paths.get("src/test/resources/lowcy/index.html"))

        when:
        def posts = lowcyGierParser.parse(html)

        then:
        posts hasSize(20)
        that(posts, containsValidPosts())
    }
}
