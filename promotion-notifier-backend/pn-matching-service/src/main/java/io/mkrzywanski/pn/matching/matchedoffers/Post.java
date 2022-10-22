package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public final class Post {
    private UUID id;
    private String link;
    private Collection<Offer> offers;

    Optional<Offer> getOfferById(final UUID id) {
        return offers.stream().filter(offer -> offer.getId().equals(id)).findAny();
    }
}
