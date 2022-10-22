package io.mkrzywanski.pn.user.app.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
class UserNotFoundException extends RuntimeException {
    private final UUID userId;

    UserNotFoundException(final UUID userId) {
        this.userId = userId;
    }
}
