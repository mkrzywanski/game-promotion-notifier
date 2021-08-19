package io.mkrzywanski.pn.matching.user;

import lombok.Value;

import java.util.UUID;

@Value
public class UserDetails {
    UUID userId;
    String username;
    String firstName;
    String email;
}
