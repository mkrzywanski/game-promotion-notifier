package io.mkrzywanski.pn.user.app.domain;

import io.mkrzywanski.pn.user.app.api.CreateUserRequest;
import io.mkrzywanski.pn.user.app.api.UserCreatedResponse;
import io.mkrzywanski.pn.user.app.infra.UserServiceConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(UserServiceConstants.Paths.USERS)
public class UserEndpoint {

    private final UserFacade userFacade;

    public UserEndpoint(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCreatedResponse> create(@Validated @RequestBody final CreateUserRequest createUserRequest) {
        final var userCreatedResponse = userFacade.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreatedResponse);
    }

    @GetMapping(path = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable final UUID userId) {
        return userFacade.get(userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
