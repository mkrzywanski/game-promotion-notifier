package io.mkrzywanski.gpn.user.app.adapters.web;

import io.mkrzywanski.gpn.user.*;
import io.mkrzywanski.gpn.user.app.api.CreateUserRequest;
import io.mkrzywanski.gpn.user.app.api.UserCreatedResponse;
import io.mkrzywanski.gpn.user.app.api.UserDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserFacade {

    private final UserService userService;

    UserFacade(final UserService userService) {
        this.userService = userService;
    }

    UserCreatedResponse create(final CreateUserRequest createUserRequest) {
        final var newUserDetails = new NewUserDetails(createUserRequest.getUserName(), EmailAddress.of(createUserRequest.getEmail()), createUserRequest.getUserName());
        final var userId = userService.create(newUserDetails);
        return new UserCreatedResponse(userId.asUuid().toString());
    }

    Optional<UserDetailsResponse> get(final UUID uuid) {
        return userService.get(UserId.of(uuid))
                .map(UserDetailsResponse::from);
    }
}
