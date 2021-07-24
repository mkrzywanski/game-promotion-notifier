package io.mkrzywanski.gpn.scrapper.app.integ.publishing

import io.mkrzywanski.gpn.scrapper.app.adapters.persistance.NewPostOutboxMessage
import io.mkrzywanski.gpn.scrapper.app.adapters.persistance.PostMongoModel
import io.mkrzywanski.gpn.scrapper.app.adapters.publishing.NewPostsPublisherAdapter
import io.mkrzywanski.gpn.scrapper.domain.post.Hash
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await

import java.time.Instant

@SpringBootTest(classes = [NewPostPublishingITConfig])
class GameHunterPublishingSpec extends Specification {

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    NewPostsPublisherAdapter newPostsPublisherAdapter

    @Autowired
    MongoTemplate mongoTemplate

    @Autowired
    NewPostConsumer newPostConsumer

    def "should publish new posts on queue"() {
        given:
        def existingPostsIds = existingPosts()
        existingPostsToPublish(existingPostsIds)

        when: "publishing is performed"
        newPostsPublisherAdapter.publish()

        then:
        postsArePublishedToQueue()

        and:
        transactionalOutboxIsEmpty()
    }

    private postsArePublishedToQueue() {
        await().atMost(2, TimeUnit.SECONDS).until { newPostConsumer.messages.size() == 1 }
        true
    }

    private boolean transactionalOutboxIsEmpty() {
        mongoTemplate.findAll(NewPostOutboxMessage).size() == 0
    }

    def existingPosts() {
        def uuid = UUID.fromString("6687f8fb-33cf-4217-9bcd-d98ce3328dda")
        def postMongoModel = new PostMongoModel(uuid, Hash.compute("test").toString(), "game-hunter", List.of(), Instant.now())
        mongoTemplate.save(postMongoModel)
        return List.of(uuid)
    }

    void existingPostsToPublish(List<UUID> postIds) {
        postIds.stream().map(postId -> new NewPostOutboxMessage(postId, Instant.now())).forEach(mongoTemplate::save)
    }
}
