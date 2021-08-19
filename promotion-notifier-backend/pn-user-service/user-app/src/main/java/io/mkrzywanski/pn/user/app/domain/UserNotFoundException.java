package io.mkrzywanski.pn.user.app.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final UUID userId;

    public UserNotFoundException(final UUID userId) {
        this.userId = userId;
    }
}
