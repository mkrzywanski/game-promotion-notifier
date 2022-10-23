package io.mkrzywanski.pn.matching.user;

import io.mkrzywanski.pn.matching.user.api.UserDetails;

import java.util.UUID;

public interface UserSerivceClient {
    UserDetails getUserDetails(UUID userId);
}
