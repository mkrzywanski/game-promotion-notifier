package io.mkrzywanski.gpn.scrapper.domain.post;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

public class NumberGamePrice implements GamePrice {

    private static final Map<String, Currency> SUPPORTED_CURRENCIES = Map.of(
            "€", Currencies.EUR,
            "zł", Currencies.PLN
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
        final String priceString = sanitize(split[0]);
        final BigDecimal price = new BigDecimal(priceString);
        return new NumberGamePrice(currency, price);
    }

    private static String sanitize(final String price) {
        return price.contains(",") ? price.replace(",", ".") : price;
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
