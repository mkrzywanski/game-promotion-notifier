package io.mkrzywanski.gpn.scrapper.domain.post;

import java.util.List;
import java.util.Set;

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
        final Set<PostId> newPostsIds = postTransactionalOutboxRepository.poll();
        final List<Post> byIds = postRepository.findByIds(newPostsIds);
        postPublisher.publish(byIds);
    }
}
