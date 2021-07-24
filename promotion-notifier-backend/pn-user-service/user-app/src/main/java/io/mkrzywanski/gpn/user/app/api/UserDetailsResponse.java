package io.mkrzywanski.gpn.user.app.api;

import io.mkrzywanski.gpn.user.User;
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

    public static UserDetailsResponse from(final User user) {
        return UserDetailsResponse.builder()
                .email(user.getEmailAddress().asString())
                .firstName(user.getFirstName())
                .username(user.getUsername())
                .userId(user.getUserId().asUuid())
                .build();
    }
}
