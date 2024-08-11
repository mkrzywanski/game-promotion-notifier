package io.mkrzywanski.pn.matching.infra.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PostsQueueConfig {

    @Value("${gpn.queue.name}")
    private String postsQueue;

    @Bean
    Queue postsQueue() {
        return new Queue(postsQueue, true);
    }

    @Bean
    DirectExchange postsExchange() {
        return new DirectExchange("posts-queue");
    }

    @Bean
    Binding postsBinding() {
        return BindingBuilder.bind(postsQueue()).to(postsExchange()).with(postsQueue);
    }


    @Bean
    RabbitTemplateCustomizer a() {
        return rabbitTemplate -> {
            rabbitTemplate.setMandatory(true);
            rabbitTemplate.setChannelTransacted(true);
            rabbitTemplate.setReturnsCallback(returned -> {
                System.out.println("Returned : " + returned);
            });
            rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
                System.out.println("ack :" + ack);
            });
        };
    }
}
