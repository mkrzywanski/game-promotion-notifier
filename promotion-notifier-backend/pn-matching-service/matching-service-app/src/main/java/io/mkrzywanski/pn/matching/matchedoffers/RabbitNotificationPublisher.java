package io.mkrzywanski.pn.matching.matchedoffers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitNotificationPublisher implements NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public RabbitNotificationPublisher(final RabbitTemplate rabbitTemplate,
                                       @Value("gpn.matching-service.publishing.queue.name") final String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void publish(final NewOffersNotification newOffersNotification) {
        rabbitTemplate.convertAndSend(queueName, newOffersNotification);
    }

}
