package io.mkrzywanski.gpn.email.infra;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableRabbit
public class RabbitConfig implements RabbitListenerConfigurer {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private RabbitInfrastructureProperties rabbitInfrastructureProperties;

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(rabbitInfrastructureProperties.getExchange());
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(rabbitInfrastructureProperties.getQueue())
                .withArgument("x-dead-letter-exchange", rabbitInfrastructureProperties.getDeadLetterExchange())
                .withArgument("x-dead-letter-routing-key", rabbitInfrastructureProperties.getDeadLetterQueue())
                .build();
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(rabbitInfrastructureProperties.getDeadLetterExchange());
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(rabbitInfrastructureProperties.getDeadLetterQueue()).build();
    }

    @Bean
    Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(rabbitInfrastructureProperties.getDeadLetterQueue());
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(rabbitInfrastructureProperties.getQueue());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }
}
