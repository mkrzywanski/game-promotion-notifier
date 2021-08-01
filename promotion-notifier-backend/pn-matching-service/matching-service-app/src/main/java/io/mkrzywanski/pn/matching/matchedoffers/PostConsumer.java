package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostConsumer {

    @RabbitListener(queues = "${gpn.queue.name}")
    public void consume(final Post json) {
        log.info("consuming");
    }

}
