package io.mkrzywanski.pn.matching.infa.http;

import io.mkrzywanski.pn.webservice.common.error.ErrorResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClientCommunicationException extends RuntimeException {
    private final ErrorResponse errorResponse;

    @Builder
    public ClientCommunicationException(final ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
