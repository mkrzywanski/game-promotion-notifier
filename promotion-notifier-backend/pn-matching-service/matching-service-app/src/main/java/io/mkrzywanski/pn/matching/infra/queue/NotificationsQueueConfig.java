package io.mkrzywanski.pn.matching.infra.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationsQueueConfig {

    @Value("${gpn.matching-service.publishing.queue.name}")
    private String notificationQueue;

    @Bean
    public Queue notificationsQueue() {
        return new Queue(notificationQueue, true);
    }

}
