package io.mkrzywanski.gpn.user.app.api;

import io.mkrzywanski.gpn.user.UserConstraints;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateUserRequest {

    @NotBlank
    @Max(UserConstraints.MAX_FIRSTNAME_LENGTH)
    private String firstName;

    @NotBlank
    @Max(UserConstraints.MAX_USERNAME_LENGTH)
    private String userName;

    @NotBlank
    @Email
    private String email;

}
