package io.mkrzywanski.gpn.scrapper.app.adapters.persistance;

import io.mkrzywanski.gpn.scrapper.domain.post.GameOffer;
import io.mkrzywanski.gpn.scrapper.domain.post.GamePrice;

public class GameOfferModel {

    private final String gameName;
    private final PriceInfo priceInfo;
    private final String link;

    public GameOfferModel(final String gameName, final PriceInfo priceInfo, final String link) {
        this.gameName = gameName;
        this.priceInfo = priceInfo;
        this.link = link;
    }

    public String getGameName() {
        return gameName;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public String getLink() {
        return link;
    }

    static GameOfferModel fromDomain(final GameOffer gameOffer) {
        final GamePrice gamePrice = gameOffer.getGamePrice();
        final PriceInfo priceInfo = PriceInfo.fromDomain(gamePrice);
        return new GameOfferModel(gameOffer.getGameName(), priceInfo, gameOffer.getLink());
    }

    GameOffer toDomain() {
        return new GameOffer(gameName, priceInfo.toDomain(), link);
    }
}
