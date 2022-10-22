package io.mkrzywanski.pn.user.app.api;

import io.mkrzywanski.pn.user.app.domain.UserConstraints;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
