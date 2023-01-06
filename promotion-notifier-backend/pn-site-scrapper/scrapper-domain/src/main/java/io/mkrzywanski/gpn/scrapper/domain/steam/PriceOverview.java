package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.annotation.JsonProperty;

record PriceOverview(String currency, int initial, int discount_percent, @JsonProperty("final") int finalPrice) {
}
