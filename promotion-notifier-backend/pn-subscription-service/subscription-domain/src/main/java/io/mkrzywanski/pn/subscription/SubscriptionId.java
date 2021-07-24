package io.mkrzywanski.pn.subscription;

import java.util.UUID;

public class SubscriptionId {

    private final UUID uuid;

    public SubscriptionId(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID asUuid() {
        return uuid;
    }
}
