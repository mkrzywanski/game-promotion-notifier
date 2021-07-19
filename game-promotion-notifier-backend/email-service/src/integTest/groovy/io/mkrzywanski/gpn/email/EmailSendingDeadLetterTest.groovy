package io.mkrzywanski.gpn.email

import io.mkrzywanski.gpn.email.config.IntegrationTestConfig
import io.mkrzywanski.gpn.email.domain.NewPostsMatchedEmailService
import io.mkrzywanski.gpn.email.infra.RabbitInfrastructureProperties
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.stereotype.Component
import spock.lang.Specification

import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootTest(classes = [IntegrationTestConfig])
class EmailSendingDeadLetterTest extends Specification {

    @MockBean
    NewPostsMatchedEmailService newPostsMatchedEmailService

    @Autowired
    RabbitInfrastructureProperties properties

    @Autowired
    RabbitTemplate rabbitTemplate

    @Autowired
    RabbitDeadLetterConsumer rabbitDeadLetterConsumer

    def "should publish dead letter when consumed message is not valid json"() {
        when:
        wrongMessageIsPublishedToQueue()
        then:
        wrongMessageIsPublishedToDeadLetterQueue()
    }

    private boolean wrongMessageIsPublishedToDeadLetterQueue() {
        rabbitDeadLetterConsumer.consumedCount() == 1
    }

    private wrongMessageIsPublishedToQueue() {
        rabbitTemplate.convertSendAndReceive(properties.getExchange(), properties.getQueue(), "wrong json lol")
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
    }
}
