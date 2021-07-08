package io.mkrzywanski.gpn.scrapper.domain.post;

import java.util.List;

public interface PostPublisher {
    void publish(List<Post> byIds);
}
