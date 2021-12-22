package io.mkrzywanski.pn.subscription;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor
public class Post {
    UUID postId;
    List<Offer> offers;

    private Post() {
        this(UUID.fromString("00000000-0000-0000-0000-000000000000"), List.<Offer>of());
    }

}
