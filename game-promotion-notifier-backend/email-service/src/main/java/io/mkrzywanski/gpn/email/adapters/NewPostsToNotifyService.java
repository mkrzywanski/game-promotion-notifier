package io.mkrzywanski.gpn.email.adapters;

import io.mkrzywanski.gpn.email.api.NewOffersNotificationData;
import io.mkrzywanski.gpn.email.domain.NewPostsMatchedEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NewPostsToNotifyService {

    private final NewPostsMatchedEmailService newPostsMatchedEmailService;

    public NewPostsToNotifyService(final NewPostsMatchedEmailService newPostsMatchedEmailService) {
        this.newPostsMatchedEmailService = newPostsMatchedEmailService;
    }

    @RabbitListener(queues = "${gpn.queueName}")
    public void consume(final NewOffersNotificationData newOffersNotificationData) {
        newPostsMatchedEmailService.send(newOffersNotificationData);
    }
}
