package io.mkrzywanski.pn.user.app.api;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class CreateUserRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    @Size(max = UserConstraints.MAX_FIRSTNAME_LENGTH)
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Size(max = UserConstraints.MAX_USERNAME_LENGTH)
    private String userName;

    @NotBlank
    @Email
    private String email;

}
