package io.mkrzywanski.gpn.user.app.adapters;

import io.mkrzywanski.gpn.user.*;
import io.mkrzywanski.gpn.user.app.api.CreateUserRequest;
import io.mkrzywanski.gpn.user.app.api.UserCreatedResponse;
import org.springframework.stereotype.Component;

@Component
class UserFacade {

    private final UserService userService;

    UserFacade(final UserService userService) {
        this.userService = userService;
    }

    UserCreatedResponse create(final CreateUserRequest createUserRequest) {
        final NewUserDetails newUserDetails = new NewUserDetails(createUserRequest.getUserName(), EmailAddress.of(createUserRequest.getEmail()), createUserRequest.getUserName());
        final UserId userId = userService.create(newUserDetails);
        return new UserCreatedResponse(userId.asString());
    }
}
