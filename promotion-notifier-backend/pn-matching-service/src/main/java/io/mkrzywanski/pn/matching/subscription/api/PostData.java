package io.mkrzywanski.pn.matching.subscription.api;

import lombok.Value;

import java.util.Collection;
import java.util.UUID;

@Value
public class PostData {
    UUID postId;
    Collection<OfferData> offers;

}
