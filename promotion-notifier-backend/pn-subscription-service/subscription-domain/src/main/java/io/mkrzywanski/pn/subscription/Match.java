package io.mkrzywanski.pn.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Match {

    private final UUID userId;
    private final UUID postId;
    private final UUID offerId;

}
