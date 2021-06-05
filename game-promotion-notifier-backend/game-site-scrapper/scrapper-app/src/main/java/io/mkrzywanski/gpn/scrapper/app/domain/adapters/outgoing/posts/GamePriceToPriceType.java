package io.mkrzywanski.gpn.scrapper.app.domain.adapters.outgoing.posts;

import io.mkrzywanski.gpn.scrapper.domain.post.*;

import java.util.Map;
import java.util.Optional;

class GamePriceToPriceType {

    private static final Map<Class<? extends GamePrice>, PriceType> PRICE_TYPES = Map.of(
            CompositeGamePrice.class, PriceType.MULTI,
            EmptyGamePrice.class, PriceType.NOT_AVAILABLE,
            FreeGamePrice.class, PriceType.FREE,
            NumberGamePrice.class, PriceType.SINGLE
    );

    static PriceType forGamePrice(final GamePrice gamePrice) {
        return Optional.ofNullable(PRICE_TYPES.get(gamePrice.getClass()))
                .orElseThrow(IllegalArgumentException::new);
    }
}
