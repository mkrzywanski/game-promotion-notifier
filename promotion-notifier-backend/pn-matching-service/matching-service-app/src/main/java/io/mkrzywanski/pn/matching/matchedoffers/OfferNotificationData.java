package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Value
public class OfferNotificationData {
    String name;
    String url;
    Map<Currency, BigDecimal> prices;

}
