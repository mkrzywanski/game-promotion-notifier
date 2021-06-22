package io.mkrzywanski.gpn.user.app.adapters;

import io.mkrzywanski.gpn.user.*;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    private final UserService userService;

    public UserFacade(final UserService userService) {
        this.userService = userService;
    }

    public UserCreatedResponse create(final CreateUserRequest createUserRequest) {
        final NewUserDetails newUserDetails = new NewUserDetails(createUserRequest.getUserName(), EmailAddress.of(createUserRequest.getEmail()), createUserRequest.getUserName());
        final UserId userId = userService.create(newUserDetails);
        return new UserCreatedResponse(userId.asString());
    }
}
