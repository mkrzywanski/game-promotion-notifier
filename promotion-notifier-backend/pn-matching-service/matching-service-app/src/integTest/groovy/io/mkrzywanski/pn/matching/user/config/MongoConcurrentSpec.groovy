package io.mkrzywanski.pn.matching.user.config

import io.mkrzywanski.pn.matching.matchedoffers.MatchesRepository
import io.mkrzywanski.pn.matching.matchedoffers.MongoMatchesRepository
import io.mkrzywanski.pn.matching.matchedoffers.OfferEntity
import io.mkrzywanski.pn.matching.matchedoffers.PostEntity
import io.mkrzywanski.pn.matching.matchedoffers.UserOfferMatches
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import spock.lang.Specification

@SpringBootTest(classes = [MongoConfig, MongoMatchesRepository])
@EnableMongoRepositories
@EnableAutoConfiguration
class MongoConcurrentTestTest extends Specification {

    @Autowired
    MatchesRepository matchesRepository

    def "test"() {
        given:
        UUID uuid = UUID.randomUUID()
        UUID post1id = UUID.randomUUID()
        UUID post2id = UUID.randomUUID()
        UserOfferMatches userOfferMatches = new UserOfferMatches(uuid, new HashSet<PostEntity>(Set.of(new PostEntity(post1id, "aaa", Set.of(new OfferEntity("aa", new HashMap<Currency, BigDecimal>(), ""))))))
        matchesRepository.saveAll(List.of(userOfferMatches))
        def all = matchesRepository.findAll()
        all.first().getPostEntities().add(new PostEntity(post2id, "bbb", Set.of(new OfferEntity("bbb", new HashMap<Currency, BigDecimal>(), "aaa"))))
        matchesRepository.saveOrUpdate(all)

        when:
        matchesRepository.removeAlreadyNotifiedPosts(List.of(userOfferMatches))
        then:
        def all1 = matchesRepository.findAll()
        println "done"
    }
}
