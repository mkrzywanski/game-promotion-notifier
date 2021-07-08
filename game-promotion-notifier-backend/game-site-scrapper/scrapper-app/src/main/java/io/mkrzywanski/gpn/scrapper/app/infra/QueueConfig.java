package io.mkrzywanski.gpn.scrapper.app.infra;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class QueueConfig {

    @Value("${gpn.queue.name}")
    private String queueName;

    @Bean
    public Queue postsQueue() {
        return new Queue(queueName, true);
    }

}
