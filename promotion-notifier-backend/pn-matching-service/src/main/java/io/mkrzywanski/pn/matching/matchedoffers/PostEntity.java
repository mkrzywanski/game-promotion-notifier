package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
class PostEntity {
    private UUID postId;
    private String link;
    private Set<OfferEntity> offerEntities;
}
