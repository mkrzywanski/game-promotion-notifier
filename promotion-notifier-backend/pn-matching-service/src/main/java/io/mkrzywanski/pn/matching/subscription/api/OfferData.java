package io.mkrzywanski.pn.matching.subscription.api;

import lombok.Value;

import java.util.UUID;

@Value
public class OfferData {
    UUID id;
    String text;
}
