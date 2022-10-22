package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostNotificationData {
    private String link;
    private List<OfferNotificationData> offerNotificationData;
}
