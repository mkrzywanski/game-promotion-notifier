package io.mkrzywanski.pn.email


import io.mkrzywanski.pn.email.domain.NewPostsMatchedEmailService
import io.mkrzywanski.pn.email.api.NewOffersNotificationData
import io.mkrzywanski.pn.email.config.IntegrationTestConfig
import io.mkrzywanski.pn.email.infra.RabbitInfrastructureProperties
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.stereotype.Component
import spock.lang.Specification

import java.util.concurrent.CopyOnWriteArrayList

@SpringBootTest(classes = [IntegrationTestConfig])
class EmailSendingValidationTest extends Specification {

    @MockBean
    NewPostsMatchedEmailService newPostsMatchedEmailService

    @Autowired
    RabbitInfrastructureProperties properties

    @Autowired
    RabbitTemplate rabbitTemplate

    @Autowired
    RabbitDeadLetterConsumer rabbitDeadLetterConsumer

    void cleanup() {
        rabbitDeadLetterConsumer.reset()
    }

    def "should publish dead letter when consumed message is not valid json"() {
        when:
        invalidJsonIsPublishedToQueue()
        then:
        wrongMessageIsPublishedToDeadLetterQueue()
    }

    def "should publish dead letter when message is invalid due to field validation"() {
        when:
        invalidObjectIsPublishedToQueue()
        then:
        wrongMessageIsPublishedToDeadLetterQueue()
    }

    private boolean wrongMessageIsPublishedToDeadLetterQueue() {
        rabbitDeadLetterConsumer.consumedCount() == 1
    }

    private invalidObjectIsPublishedToQueue() {
        def newOffersNotificationData = new NewOffersNotificationData(null, null)
        sendToQueue(newOffersNotificationData)
    }

    private invalidJsonIsPublishedToQueue() {
        sendToQueue("wrong json")
    }

    private sendToQueue(Object object) {
        rabbitTemplate.convertSendAndReceive(properties.getExchange(), properties.getQueue(), object)
    }

    @Component
    static class RabbitDeadLetterConsumer {

        private final List<Message> messages = new CopyOnWriteArrayList<>()

        @RabbitListener(queues = '${gpn.rabbitmq.dead-letter-queue}')
        void consume(Message message) {
            messages.add(message)
        }

        int consumedCount() {
            return messages.size()
        }

        def reset() {
            messages.clear()
        }
    }
}
