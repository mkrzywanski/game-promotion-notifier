package io.mkrzywanski.gpn.scrapper.domain.post.price;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public interface GamePrice {
    String asString();
    Map<Currency, BigDecimal> asMap();
}
