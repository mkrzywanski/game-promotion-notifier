package io.mkrzywanski.gpn.subscription.app.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.UUID;

public class CreateSubscriptionRequest {

    private UUID userId;
    private Set<SubscriptionItem> itemSet;

    @JsonCreator
    public CreateSubscriptionRequest(@JsonProperty("userId") final UUID userId,
                                     @JsonProperty("itemSet") final Set<SubscriptionItem> itemSet) {
        this.userId = userId;
        this.itemSet = itemSet;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<SubscriptionItem> getItemSet() {
        return itemSet;
    }
}
