package io.mkrzywanski.pn.email.contract;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

record SimpleMessageVerifier(
        RabbitTemplate rabbitTemplate) implements MessageVerifierSender<Message> {

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
        if (payload instanceof String raw) {
            final Message message = MessageBuilder.withBody(raw.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();
            send(message, destination, contract);
        } else {
            throw new IllegalStateException("Cannot send message");
        }
    }

}
