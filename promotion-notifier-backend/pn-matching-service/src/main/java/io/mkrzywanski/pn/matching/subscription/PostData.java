package io.mkrzywanski.pn.matching.subscription;

import lombok.Value;

import java.util.Collection;
import java.util.UUID;

@Value
public class PostData {
    UUID postId;
    Collection<OfferData> offers;

}
