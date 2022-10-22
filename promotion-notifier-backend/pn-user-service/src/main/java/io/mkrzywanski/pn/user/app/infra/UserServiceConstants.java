package io.mkrzywanski.pn.user.app.infra;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserServiceConstants {
    public static final String USER_SERVICE = "user-service";

    public static final class Paths {
        public static final String VERSION = "v1";
        public static final String USERS = VERSION + "/users";
    }
}
