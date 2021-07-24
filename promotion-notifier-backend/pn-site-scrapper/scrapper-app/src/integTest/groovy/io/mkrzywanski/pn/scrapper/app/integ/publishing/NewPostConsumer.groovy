package io.mkrzywanski.pn.scrapper.app.integ.publishing

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

import java.util.concurrent.CopyOnWriteArrayList

@Component
class NewPostConsumer {

    private final List<String> messages;

    NewPostConsumer() {
        messages = new CopyOnWriteArrayList<>();
    }

    @RabbitListener(queues = '${gpn.queue.name}')
    void consume(final String json) {
        messages.add(json);
    }

    List<String> getMessages() {
        return messages;
    }
}
