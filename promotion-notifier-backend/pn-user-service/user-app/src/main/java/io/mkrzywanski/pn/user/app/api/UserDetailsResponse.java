package io.mkrzywanski.pn.user.app.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserDetailsResponse {

    UUID userId;
    String username;
    String firstName;
    String email;

}
