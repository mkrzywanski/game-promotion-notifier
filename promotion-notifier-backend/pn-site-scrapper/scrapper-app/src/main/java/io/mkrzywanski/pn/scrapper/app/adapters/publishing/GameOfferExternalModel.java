package io.mkrzywanski.pn.scrapper.app.adapters.publishing;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

@Value
public class GameOfferExternalModel {
    String gameName;
    Map<Currency, BigDecimal> gamePrice;
    String link;

    static GameOfferExternalModel fromDomain(final GameOffer gameOffer) {
        return new GameOfferExternalModel(gameOffer.getGameName(), gameOffer.getGamePrice().asMap(), gameOffer.getLink());
    }
}
