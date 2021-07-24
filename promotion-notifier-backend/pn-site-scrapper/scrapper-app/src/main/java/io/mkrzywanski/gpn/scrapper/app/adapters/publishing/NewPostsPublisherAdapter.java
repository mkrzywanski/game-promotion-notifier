package io.mkrzywanski.gpn.scrapper.app.adapters.publishing;

import io.mkrzywanski.gpn.scrapper.domain.post.NewPostPublishing;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NewPostsPublisherAdapter {

    private final NewPostPublishing newPostPublishing;

    public NewPostsPublisherAdapter(final NewPostPublishing newPostPublishing) {
        this.newPostPublishing = newPostPublishing;
    }

    @Scheduled(cron = "${gpn.scheduling.publishing.cron}")
    @Transactional
    public void publish() {
        newPostPublishing.publish();
    }
}
