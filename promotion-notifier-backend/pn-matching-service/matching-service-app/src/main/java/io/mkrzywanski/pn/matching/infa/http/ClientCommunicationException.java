package io.mkrzywanski.pn.matching.infa.http;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientCommunicationException extends RuntimeException {
    private final String serviceName;
    private final HttpStatus httpStatus;

    @Builder
    public ClientCommunicationException(final String serviceName, final HttpStatus httpStatus, final String message) {
        super(message);
        this.serviceName = serviceName;
        this.httpStatus = httpStatus;
    }
}
