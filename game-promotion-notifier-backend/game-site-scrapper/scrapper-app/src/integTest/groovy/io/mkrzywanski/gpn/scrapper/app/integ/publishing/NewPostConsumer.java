package io.mkrzywanski.gpn.scrapper.app.integ.publishing;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NewPostConsumer {

    private final List<String> messages;

    public NewPostConsumer() {
        messages = new CopyOnWriteArrayList<>();
    }

    @RabbitListener(queues = "${gpn.queue.name}")
    public void consume(final String json) {
        messages.add(json);
    }

    public List<String> getMessages() {
        return messages;
    }
}
