package io.mkrzywanski.gpn.scrapper.domain.post;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Currency;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class CompositeGamePrice implements GamePrice {

    private static final BinaryOperator<BigDecimal> TAKE_FIRST = (b, b1) -> b;
    private static final Comparator<Map.Entry<Currency, BigDecimal>> CURRENCY_COMPARATOR = Comparator.comparing(entry -> entry.getKey().getCurrencyCode());
    private final Collection<GamePrice> prices;

    public CompositeGamePrice(final Collection<GamePrice> prices) {
        this.prices = prices;
    }

    @Override
    public String asString() {
        return prices.stream()
                .map(GamePrice::asString)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Map<Currency, BigDecimal> asMap() {
        return prices.stream()
                .map(GamePrice::asMap)
                .flatMap(map -> map.entrySet().stream())
                .sorted(CURRENCY_COMPARATOR)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, TAKE_FIRST));
    }
}
