package io.mkrzywanski.gpn.scrapper.domain.post;

import lombok.Value;

import java.util.UUID;

@Value
public class PostId {
    UUID id;

    public static PostId from(final UUID uuid) {
        return new PostId(uuid);
    }

    public static PostId generate() {
        return new PostId(UUID.randomUUID());
    }
}
