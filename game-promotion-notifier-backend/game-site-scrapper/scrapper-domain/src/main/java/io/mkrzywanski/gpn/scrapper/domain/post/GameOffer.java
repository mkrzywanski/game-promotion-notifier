package io.mkrzywanski.gpn.scrapper.domain.post;

public class GameOffer {

    private final String gameName;
    private final GamePrice gamePrice;
    private final String link;

    public GameOffer(final String gameName, final GamePrice gamePrice, final String link) {
        this.gameName = gameName;
        this.gamePrice = gamePrice;
        this.link = link;
    }

    public String getGameName() {
        return gameName;
    }

    public GamePrice getGamePrice() {
        return gamePrice;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "GameOffer{" +
                "gameName='" + gameName + '\'' +
                ", gamePrice=" + gamePrice.asString() +
                ", link='" + link + '\'' +
                '}';
    }
}
