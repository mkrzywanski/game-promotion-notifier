package io.mkrzywanski.gpn.scrapper.domain.post.price;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;

public class EmptyGamePrice implements GamePrice {

    @Override
    public String asString() {
        return "not available";
    }

    @Override
    public Map<Currency, BigDecimal> asMap() {
        return Collections.emptyMap();
    }
}
