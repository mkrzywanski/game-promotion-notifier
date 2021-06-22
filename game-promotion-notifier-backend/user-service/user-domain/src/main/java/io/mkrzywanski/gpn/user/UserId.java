package io.mkrzywanski.gpn.user;

import java.util.Objects;
import java.util.UUID;

public class UserId {

    private final UUID uuid;

    UserId(final UUID uuid) {
        this.uuid = uuid;
    }

    public static UserId of(final UUID value) {
        return new UserId(value);
    }

    public static UserId of(final String value) {
        return new UserId(UUID.fromString(value));
    }

    static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public String asString() {
        return this.uuid.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserId userId = (UserId) o;
        return uuid.equals(userId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
