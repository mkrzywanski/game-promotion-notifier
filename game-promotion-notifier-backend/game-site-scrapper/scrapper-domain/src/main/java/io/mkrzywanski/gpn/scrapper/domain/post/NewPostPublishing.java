package io.mkrzywanski.gpn.scrapper.domain.post;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class NewPostPublishing {

    private final PostTransactionalOutboxRepository postTransactionalOutboxRepository;
    private final PostRepository postRepository;
    private final PostPublisher postPublisher;

    public NewPostPublishing(final PostTransactionalOutboxRepository postsToPublishRepo,
                             final PostRepository postRepository,
                             final PostPublisher postPublisher) {
        this.postTransactionalOutboxRepository = postsToPublishRepo;
        this.postRepository = postRepository;
        this.postPublisher = postPublisher;
    }

    public void publish() {
        log.info("Publishing new posts");
        final Set<PostId> newPostsIds = postTransactionalOutboxRepository.poll();
        log.info("Posts to publish : {}", newPostsIds.size());
        final List<Post> byIds = postRepository.findByIds(newPostsIds);
        postPublisher.publish(byIds);
        postTransactionalOutboxRepository.delete(newPostsIds);
    }
}
