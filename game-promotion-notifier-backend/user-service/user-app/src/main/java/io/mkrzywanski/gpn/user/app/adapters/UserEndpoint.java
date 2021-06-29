package io.mkrzywanski.gpn.user.app.adapters;

import io.mkrzywanski.gpn.user.app.api.CreateUserRequest;
import io.mkrzywanski.gpn.user.app.api.UserCreatedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/users")
class UserEndpoint {

    private final UserFacade userFacade;

    UserEndpoint(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCreatedResponse> create(final CreateUserRequest createUserRequest) {
        final UserCreatedResponse userCreatedResponse = userFacade.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreatedResponse);
    }
}
