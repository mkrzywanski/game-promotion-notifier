package io.mkrzywanski.pn.scrapper.app.adapters.persistance;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document("new-posts-outbox")
@Value
public class NewPostOutboxMessage {
    @Id
    UUID postId;
    Instant createdAt;
}
