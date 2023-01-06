package io.mkrzywanski.pn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.price.CompositeGamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.price.EmptyGamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.price.FreeGamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.price.GamePrice;
import io.mkrzywanski.gpn.scrapper.domain.post.NumberGamePrice;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PriceInfo {

    private final Map<Currency, BigDecimal> prices;
    private final PriceType priceType;

    public PriceInfo(final Map<Currency, BigDecimal> prices, final PriceType priceType) {
        this.prices = prices;
        this.priceType = priceType;
    }

    static PriceInfo fromDomain(final GamePrice gamePrice) {
        final PriceType priceType = GamePriceToPriceType.forGamePrice(gamePrice);
        return new PriceInfo(gamePrice.asMap(), priceType);
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
