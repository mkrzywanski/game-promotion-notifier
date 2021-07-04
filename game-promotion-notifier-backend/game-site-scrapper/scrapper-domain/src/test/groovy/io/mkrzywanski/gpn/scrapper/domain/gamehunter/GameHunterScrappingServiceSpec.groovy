package io.mkrzywanski.gpn.scrapper.domain.gamehunter


import io.mkrzywanski.gpn.scrapper.domain.post.PostObjectMother
import io.mkrzywanski.gpn.scrapper.domain.post.PostRepository
import io.mkrzywanski.gpn.scrapper.domain.post.PostTransactionalOutboxRepository
import spock.lang.Specification

import java.time.Clock
import java.time.ZonedDateTime

class GameHunterScrappingServiceSpec extends Specification {

    def stub = Stub(GameHunterScrapper)
    def postRepository = Mock(PostRepository)
    def pub = Mock(PostTransactionalOutboxRepository)
    def service = new GameHunterScrappingService(stub, postRepository, pub, Clock.systemUTC())

    def "should scrap new posts from first page"() {
        given:
        def post1 = PostObjectMother.newInstance().withSource("source1").withDatePosted(ZonedDateTime.now()).build()
        def post2 = PostObjectMother.newInstance().withSource("source2").withDatePosted(ZonedDateTime.now()).build()
        def post3 = PostObjectMother.newInstance().withSource("source3").withDatePosted(ZonedDateTime.now()).build()
        def postsFromScrapper = List.of(post1, post2, post3)
        def postsAlreadyInDb = List.of(post2, post3)
        def newPosts = postsFromScrapper - postsAlreadyInDb

        stub.scrap(1) >> postsFromScrapper

        def hashes = postsFromScrapper*.hash as HashSet
        postRepository.findByHashIn(hashes) >> postsAlreadyInDb*.hash

        when:
        service.scrap()

        then:
        1 * postRepository.saveAll(newPosts)
    }

    def "should scrap two pages when first page contains all new posts and second one new and already existing"() {

        given:
        def post1 = PostObjectMother.newInstance().withSource("source1").withDatePosted(ZonedDateTime.now()).build()
        def post2 = PostObjectMother.newInstance().withSource("source2").withDatePosted(ZonedDateTime.now()).build()
        def post3 = PostObjectMother.newInstance().withSource("source3").withDatePosted(ZonedDateTime.now()).build()

        def post4 = PostObjectMother.newInstance().withSource("source4").withDatePosted(ZonedDateTime.now()).build()
        def post5 = PostObjectMother.newInstance().withSource("source5").withDatePosted(ZonedDateTime.now()).build()
        def post6 = PostObjectMother.newInstance().withSource("source6").withDatePosted(ZonedDateTime.now()).build()

        def postsFromPage1 = List.of(post1, post2, post3)
        def postsFromPage2 = List.of(post4, post5, post6)

        def postsFromPage2AlreadyInDb = List.of(post5, post6)
        def newPostsPage2 = postsFromPage2 - postsFromPage2AlreadyInDb

        stub.scrap(1) >> postsFromPage1
        stub.scrap(2) >> postsFromPage2

        def page1postHashes = postsFromPage1*.hash as HashSet
        postRepository.findByHashIn(page1postHashes) >> List.of()

        def page2postHashes = postsFromPage2*.hash as HashSet
        postRepository.findByHashIn(page2postHashes) >> postsFromPage2AlreadyInDb*.hash

        when:
        service.scrap()

        then:
        1 * postRepository.saveAll(postsFromPage1 + newPostsPage2)
    }
}
