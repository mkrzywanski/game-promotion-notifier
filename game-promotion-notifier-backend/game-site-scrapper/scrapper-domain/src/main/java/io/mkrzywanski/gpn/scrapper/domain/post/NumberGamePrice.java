package io.mkrzywanski.gpn.scrapper.domain.post;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class NumberGamePrice implements GamePrice {

    private static final Map<String, Currency> SUPPORTED_CURRENCIES = Map.of(
            "€", Currency.getInstance("EUR"),
            "zł", Currency.getInstance("PLN")
    );

    private final Currency currency;
    private final BigDecimal value;

    public NumberGamePrice(final Currency currency, final BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    public static NumberGamePrice fromString(final String input) {
        final String[] split = input.split(" ");
        final Currency currency = SUPPORTED_CURRENCIES.get(split[1]);
        final BigDecimal value = new BigDecimal(split[0]);
        return new NumberGamePrice(currency, value);
    }

    @Override
    public String asString() {
        return String.format("%s %s", value, currency);
    }

    @Override
    public Map<Currency, BigDecimal> asMap() {
        return Map.of(currency, value);
    }
}
