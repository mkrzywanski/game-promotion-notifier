package io.mkrzywanski.pn.matching.subscription;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Match {
    private UUID userId;
    private UUID postId;
    private UUID offerId;
}
