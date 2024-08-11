package io.mkrzywanski.keycloak.listeners;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
@EqualsAndHashCode
public class UserCreatedEvent {

    private final String userId;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;

    static UserCreatedEvent create(final Map<String, String> details, final String userId) {
        return UserCreatedEvent.builder()
                .email(details.get("email"))
                .firstName(details.get("first_name"))
                .lastName(details.get("last_name"))
                .userName(details.get("username"))
                .userId(userId)
                .build();
    }

}
