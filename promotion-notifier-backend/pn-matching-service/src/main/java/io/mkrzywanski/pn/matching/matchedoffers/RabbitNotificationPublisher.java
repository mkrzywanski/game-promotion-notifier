package io.mkrzywanski.pn.matching.matchedoffers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RabbitNotificationPublisher implements NotificationPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitNotificationPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    RabbitNotificationPublisher(final RabbitTemplate rabbitTemplate,
                                @Value("${gpn.matching-service.publishing.queue.name}") final String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public void publish(final NewOffersNotification newOffersNotification) {
        LOG.info("Producing new notification " + newOffersNotification);
        rabbitTemplate.convertAndSend(queueName, newOffersNotification);
    }

}
