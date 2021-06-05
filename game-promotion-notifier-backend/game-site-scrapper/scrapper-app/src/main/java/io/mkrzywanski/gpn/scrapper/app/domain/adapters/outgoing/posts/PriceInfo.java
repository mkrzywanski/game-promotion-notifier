package io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts;

import io.mkrzywanski.gpn.scrapper.domain.post.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PriceInfo {

    private final Map<Currency, BigDecimal> prices;
    private final String stringValue;
    private final PriceType priceType;

    PriceInfo(final Map<Currency, BigDecimal> prices, final String stringValue, final PriceType priceType) {
        this.prices = prices;
        this.stringValue = stringValue;
        this.priceType = priceType;
    }

    static PriceInfo fromDomain(final GamePrice gamePrice) {
        final PriceType priceType = GamePriceToPriceType.forGamePrice(gamePrice);
        return new PriceInfo(gamePrice.asMap(), gamePrice.asString(), priceType);
    }

    GamePrice toDomain() {
        return switch (priceType) {
            case FREE -> new FreeGamePrice();
            case MULTI -> new CompositeGamePrice(priceStream().collect(Collectors.toList()));
            case NOT_AVAILABLE -> new EmptyGamePrice();
            case SINGLE -> priceStream().findFirst().orElseThrow();
        };
    }

    private Stream<NumberGamePrice> priceStream() {
        return prices.entrySet()
                .stream()
                .map(entry -> new NumberGamePrice(entry.getKey(), entry.getValue()));
    }
}
