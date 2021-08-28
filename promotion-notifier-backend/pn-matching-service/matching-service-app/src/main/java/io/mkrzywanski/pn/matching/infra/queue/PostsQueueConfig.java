package io.mkrzywanski.pn.matching.infra.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostsQueueConfig {

    @Value("${gpn.queue.name}")
    private String postsQueue;

    @Bean
    public Queue postsQueue() {
        return new Queue(postsQueue, true);
    }

}
