package io.mkrzywanski;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleMessageVerifier implements MessageVerifier<Message> {


    private final RabbitTemplate rabbitTemplate;

    public SimpleMessageVerifier(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Message receive(final String destination, final long timeout, final TimeUnit timeUnit,
                           final YamlContract contract) {
        return null;
    }

    @Override
    public Message receive(final String destination, final YamlContract contract) {
        return null;
    }

    @Override
    public void send(final Message message, final String destination, final YamlContract contract) {
        rabbitTemplate.send(destination, message);
    }

    @Override
    public <T> void send(final T payload, final Map<String, Object> headers,
                         final String destination, final YamlContract contract) {
        final Map<String, Object> newHeaders = headers != null ? new HashMap<>(headers) : new HashMap<>();
        final MessageProperties messageProperties = new MessageProperties();
        newHeaders.put("contentType", "application/json");
        newHeaders.forEach(messageProperties::setHeader);
        log.info("Sending a message to destination [{}] with routing key", destination);
        if (payload instanceof String raw) {
            final Message message = MessageBuilder.withBody(raw.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();
            send(message, destination, contract);
        } else {
            throw new IllegalStateException("Cannot send message");
        }
    }

}
