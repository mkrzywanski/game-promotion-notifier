package io.mkrzywanski.pn.subscription.app.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class CreateSubscriptionRequest {

    private final UUID userId;
    private final Set<SubscriptionItem> itemSet;

    @JsonCreator
    public CreateSubscriptionRequest(@JsonProperty("userId") final UUID userId,
                                     @JsonProperty("itemSet") final Set<SubscriptionItem> itemSet) {
        this.userId = userId;
        this.itemSet = itemSet;
    }
}
