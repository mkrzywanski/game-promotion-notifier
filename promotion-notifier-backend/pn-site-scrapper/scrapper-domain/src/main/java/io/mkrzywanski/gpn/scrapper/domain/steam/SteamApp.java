package io.mkrzywanski.gpn.scrapper.domain.steam;

import com.fasterxml.jackson.annotation.JsonProperty;

record SteamApp(int appid, @JsonProperty("name") String gameName) {
}
