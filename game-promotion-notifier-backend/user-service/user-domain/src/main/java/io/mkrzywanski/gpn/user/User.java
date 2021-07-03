package io.mkrzywanski.gpn.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {

    private final UserId userId;
    private final String username;
    private final String firstName;
    private final EmailAddress emailAddress;

}
