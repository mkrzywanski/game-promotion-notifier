package io.mkrzywanski.pn.matching.user;

import java.util.UUID;

public interface UserSerivceClient {
    UserDetails getUserDetails(UUID userId);
}
