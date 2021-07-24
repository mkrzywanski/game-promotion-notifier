package io.mkrzywanski.gpn.scrapper.domain.post;

import java.math.BigDecimal;
import java.util.Currency;

public class GameOfferObjectMother {

    private String gameName;
    private GamePrice gamePrice;
    private String link;

    private GameOfferObjectMother() {
        this.gameName = "testGame";
        this.gamePrice = new NumberGamePrice(Currency.getInstance("PLN"), BigDecimal.TEN);
        this.link = "http://test.pl";
    }

    public static GameOfferObjectMother newInstance() {
        return new GameOfferObjectMother();
    }

    GameOfferObjectMother withGameName(final String gameName) {
        this.gameName = gameName;
        return this;
    }

    GameOfferObjectMother withGamePrice(final GamePrice gamePrice) {
        this.gamePrice = gamePrice;
        return this;
    }


    GameOfferObjectMother withLink(final String link) {
        this.link = link;
        return this;
    }

    public GameOffer build() {
        return new GameOffer(gameName, gamePrice, link);
    }

}
