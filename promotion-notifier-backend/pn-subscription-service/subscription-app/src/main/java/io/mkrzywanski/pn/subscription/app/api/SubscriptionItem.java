package io.mkrzywanski.pn.subscription.app.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionItem {

    private final String value;

    @JsonCreator
    public SubscriptionItem(@JsonProperty("value") final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
