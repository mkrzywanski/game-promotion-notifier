package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.annotation.JsonProperty;

record GameData(@JsonProperty("price_overview") PriceOverview priceInfo) {
    static GameData empty() {
        return new GameData(new PriceOverview("PLN", -1, -1, -1));
    }
}
