package io.mkrzywanski.pn.scrapper.app.adapters.publishing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mkrzywanski.gpn.scrapper.domain.post.Post;
import io.mkrzywanski.gpn.scrapper.domain.post.PostPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;
import java.util.List;

@Component
@Slf4j
public class QueuePostPublisher implements PostPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    private final ObjectMapper objectMapper;

    public QueuePostPublisher(final RabbitTemplate rabbitTemplate,
                              final @Value("${gpn.queue.name}") String queueName,
                              final ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(final List<Post> byIds) {
        byIds.stream()
                .map(PostExternalModel::fromDomain)
                .map(this::toJson)
                .forEach(this::publish);
    }

    private void publish(final String json) {
        log.info("publishing post to queue {}. Payload : {}", queueName, json);
        rabbitTemplate.convertAndSend(queueName, json);
    }

    private String toJson(final PostExternalModel post) {
        try {
            return objectMapper.writeValueAsString(post);
        } catch (final JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
