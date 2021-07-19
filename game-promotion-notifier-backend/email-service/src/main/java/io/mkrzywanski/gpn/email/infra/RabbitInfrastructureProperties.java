package io.mkrzywanski.gpn.email.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("gpn.rabbitmq")
@AllArgsConstructor
@Getter
public class RabbitInfrastructureProperties {
    private final String exchange;
    private final String queue;
    private final String deadLetterExchange;
    private final String deadLetterQueue;
}
