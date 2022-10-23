package io.mkrzywanski.pn.matching.user.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetails {
    private UUID userId;
    private String username;
    private String firstName;
    private String email;
}
