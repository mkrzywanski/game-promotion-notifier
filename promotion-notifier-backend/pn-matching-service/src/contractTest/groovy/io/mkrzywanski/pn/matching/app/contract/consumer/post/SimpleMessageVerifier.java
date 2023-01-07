package io.mkrzywanski.pn.matching.app.contract.consumer.post;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class SimpleMessageVerifier implements MessageVerifierSender<org.springframework.messaging.Message<?>> {


    private final RabbitTemplate rabbitTemplate;

    SimpleMessageVerifier(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void send(final org.springframework.messaging.Message<?> message, final String destination, final YamlContract contract) {
        rabbitTemplate.send(destination, toMessage(message));
    }

    @Override
    public <T> void send(final T payload, final Map<String, Object> headers, final String destination, final YamlContract contract) {
        send(org.springframework.messaging.support.MessageBuilder.withPayload(payload).copyHeaders(headers).build(), destination, contract);
    }

    private Message toMessage(final org.springframework.messaging.Message<?> msg) {
        final Object payload = msg.getPayload();
        final MessageHeaders headers = msg.getHeaders();
        final Map<String, Object> newHeaders = new HashMap<>(headers);
        final MessageProperties messageProperties = new MessageProperties();
        newHeaders.forEach(messageProperties::setHeader);
        if (payload instanceof final String json) {
            return MessageBuilder.withBody(json.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();
        } else {
            throw new IllegalStateException("Payload is not a String");
        }
    }

}
