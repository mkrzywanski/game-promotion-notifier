package io.mkrzywanski.pn.user.app.domain;

import io.mkrzywanski.pn.webservice.common.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

import static io.mkrzywanski.pn.user.app.infra.UserServiceConstants.USER_SERVICE;
import static java.lang.String.format;

@ControllerAdvice
public class UserEndpointControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(final UserNotFoundException exception, final HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .serviceName(USER_SERVICE)
                .timestamp(Instant.now())
                .message(format("User with id %s not found", exception.getUserId()))
                .path(request.getPathInfo())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);

    }

}
