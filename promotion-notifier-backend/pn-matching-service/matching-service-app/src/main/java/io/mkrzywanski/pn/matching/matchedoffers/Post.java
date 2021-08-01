package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.*;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public final class Post {
    private UUID id;
    private Collection<Offer> offers;
}
