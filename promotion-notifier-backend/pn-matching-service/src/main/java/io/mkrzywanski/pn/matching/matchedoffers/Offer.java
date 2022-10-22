package io.mkrzywanski.pn.matching.matchedoffers;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public final class Offer {
    private UUID id;
    private String name;
    private Map<Currency, BigDecimal> gamePrice;
    private String link;
}
