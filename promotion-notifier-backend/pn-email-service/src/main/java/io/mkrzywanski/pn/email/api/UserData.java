package io.mkrzywanski.pn.email.api;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UserData {

    @NotNull
    UUID userId;

    @NotEmpty
    String username;

    @NotEmpty
    String firstName;

    @NotEmpty
    @Email
    String email;

}

