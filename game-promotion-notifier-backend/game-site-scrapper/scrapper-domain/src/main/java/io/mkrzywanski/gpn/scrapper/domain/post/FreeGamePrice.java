package io.mkrzywanski.gpn.scrapper.domain.post;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;

public class FreeGamePrice implements GamePrice {
    @Override
    public String asString() {
        return "free";
    }

    @Override
    public Map<Currency, BigDecimal> asMap() {
        return Collections.emptyMap();
    }
}
