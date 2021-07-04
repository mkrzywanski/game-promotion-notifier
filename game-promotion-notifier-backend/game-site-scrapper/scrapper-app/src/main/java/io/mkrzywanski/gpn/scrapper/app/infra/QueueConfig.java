package io.mkrzywanski.gpn.scrapper.app.infra;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Value("${gpn.queue.name}")
    private String queueName;

    @Bean
    public Queue postsQueue() {
        return new Queue(queueName, true);
    }

}
