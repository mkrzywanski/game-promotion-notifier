package io.mkrzywanski.gpn.scrapper.domain.post;

import io.mkrzywanski.gpn.scrapper.domain.post.price.GamePrice;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class GameOfferObjectMother {

    private UUID id;
    private String gameName;
    private GamePrice gamePrice;
    private String link;

    private GameOfferObjectMother() {
        this.id = UUID.fromString("875f4672-05ba-4a36-97eb-f9c076ee6423");
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

    GameOfferObjectMother withId(final UUID id) {
        this.id = id;
        return this;
    }
    public GameOffer build() {
        return new GameOffer(id, gameName, gamePrice, link);
    }

}
