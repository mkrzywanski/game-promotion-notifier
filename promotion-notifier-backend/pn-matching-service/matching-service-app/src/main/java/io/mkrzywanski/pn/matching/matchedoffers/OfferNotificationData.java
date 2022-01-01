package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferNotificationData {
    private String name;
    private String url;
    private Map<Currency, BigDecimal> prices;

}
