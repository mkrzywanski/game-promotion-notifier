package io.mkrzywanski.gpn.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserConstraints {
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MAX_FIRSTNAME_LENGTH = 20;
}
