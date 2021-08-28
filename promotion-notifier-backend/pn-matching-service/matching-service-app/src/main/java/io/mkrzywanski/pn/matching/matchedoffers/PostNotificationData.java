package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.Value;

import java.util.List;

@Value
public class PostNotificationData {
    String link;
    List<OfferNotificationData> offerNotificationData;
}
