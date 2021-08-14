package io.mkrzywanski.pn.user.app.api;

import io.mkrzywanski.pn.user.app.domain.UserConstraints;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateUserRequest {

    @NotBlank
    @Size(max = UserConstraints.MAX_FIRSTNAME_LENGTH)
    private String firstName;

    @NotBlank
    @Size(max = UserConstraints.MAX_USERNAME_LENGTH)
    private String userName;

    @NotBlank
    @Email
    private String email;

}
