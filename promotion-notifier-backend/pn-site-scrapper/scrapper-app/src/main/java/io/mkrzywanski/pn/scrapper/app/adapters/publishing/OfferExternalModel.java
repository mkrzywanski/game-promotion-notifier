package io.mkrzywanski.pn.scrapper.app.adapters.publishing;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

@Value
public class OfferExternalModel {
    UUID id;
    String name;
    Map<Currency, BigDecimal> gamePrice;
    String link;

    static OfferExternalModel fromDomain(final GameOffer gameOffer) {
        return new OfferExternalModel(gameOffer.getId(), gameOffer.getGameName(), gameOffer.getGamePrice().asMap(), gameOffer.getLink());
    }
}
