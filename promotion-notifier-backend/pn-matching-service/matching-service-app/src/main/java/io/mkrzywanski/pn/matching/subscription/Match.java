package io.mkrzywanski.pn.matching.subscription;

import lombok.Value;

import java.util.UUID;

@Value
public class Match {
    UUID userId;
    UUID postId;
    UUID offerId;
}
