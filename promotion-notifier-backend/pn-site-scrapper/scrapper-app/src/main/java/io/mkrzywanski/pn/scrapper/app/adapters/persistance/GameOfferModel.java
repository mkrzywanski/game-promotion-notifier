package io.mkrzywanski.pn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.GamePrice;
import lombok.Value;

@Value
public class GameOfferModel {

    String gameName;
    PriceInfo priceInfo;
    String link;

    static GameOfferModel fromDomain(final GameOffer gameOffer) {
        final GamePrice gamePrice = gameOffer.getGamePrice();
        final PriceInfo priceInfo = PriceInfo.fromDomain(gamePrice);
        return new GameOfferModel(gameOffer.getGameName(), priceInfo, gameOffer.getLink());
    }

    GameOffer toDomain() {
        return new GameOffer(gameName, priceInfo.toDomain(), link);
    }
}
