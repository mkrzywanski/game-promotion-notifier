package io.mkrzywanski.pn.user.app.adapters.web;

import io.mkrzywanski.pn.user.app.api.CreateUserRequest;
import io.mkrzywanski.pn.user.app.api.UserCreatedResponse;
import io.mkrzywanski.pn.user.app.api.UserDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/users")
class UserEndpoint {

    private final UserFacade userFacade;

    UserEndpoint(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCreatedResponse> create(@Validated @RequestBody final CreateUserRequest createUserRequest) {
        final var userCreatedResponse = userFacade.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreatedResponse);
    }

    @GetMapping(path = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsResponse> get(@PathVariable final UUID userId) {
        return userFacade.get(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
