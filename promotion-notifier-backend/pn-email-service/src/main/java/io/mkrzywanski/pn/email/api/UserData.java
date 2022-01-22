package io.mkrzywanski.pn.email.api;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

