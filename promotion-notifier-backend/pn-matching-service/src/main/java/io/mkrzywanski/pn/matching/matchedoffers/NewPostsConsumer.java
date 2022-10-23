package io.mkrzywanski.pn.matching.matchedoffers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class NewPostsConsumer {

    private final PostProcessingService postProcessingService;

    NewPostsConsumer(final PostProcessingService postProcessingService) {
        this.postProcessingService = postProcessingService;
    }

    @RabbitListener(queues = "${gpn.queue.name}")
    public void consume(final Post post) {
        postProcessingService.process(post);
    }
}
