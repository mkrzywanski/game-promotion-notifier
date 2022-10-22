package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferEntity {
    private String name;
    private Map<Currency, BigDecimal> gamePrice;
    private String link;
}
