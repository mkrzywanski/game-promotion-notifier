package io.mkrzywanski.gpn.subscription;

import java.util.UUID;

public class UserId {

    private final UUID id;

    private UserId(final UUID id) {
        this.id = id;
    }

    public static UserId of(final UUID uuid) {
        return new UserId(uuid);
    }

    public UUID asUuid() {
        return id;
    }
}
