package io.mkrzywanski.pn.email.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;


@ConfigurationProperties("gpn.rabbitmq")
@AllArgsConstructor(onConstructor_ = {@ConstructorBinding})
@Getter
public class RabbitInfrastructureProperties {
    private final String exchange;
    private final String queue;
    private final String deadLetterExchange;
    private final String deadLetterQueue;
}
