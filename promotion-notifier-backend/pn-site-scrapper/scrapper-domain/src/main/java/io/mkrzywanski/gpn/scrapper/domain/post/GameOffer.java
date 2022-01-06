package io.mkrzywanski.gpn.scrapper.domain.post;

import lombok.Value;

import java.util.UUID;

@Value
public class GameOffer {

    UUID id;
    String gameName;
    GamePrice gamePrice;
    String link;

    @Override
    public String toString() {
        return "GameOffer{" +
                "gameName='" + gameName + '\'' +
                ", gamePrice=" + gamePrice.asString() +
                ", link='" + link + '\'' +
                '}';
    }
}
