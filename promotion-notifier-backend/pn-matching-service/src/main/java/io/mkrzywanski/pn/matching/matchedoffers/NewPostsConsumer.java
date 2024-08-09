package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class NewPostsConsumer {

    private final PostProcessingService postProcessingService;

    NewPostsConsumer(final PostProcessingService postProcessingService) {
        this.postProcessingService = postProcessingService;
    }

    @RabbitListener(queues = "${gpn.queue.name}")
    public void consume(final Post post) {
        log.info("Consuming post {}", post);
        postProcessingService.process(post);
    }
}
