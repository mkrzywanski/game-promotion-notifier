package io.mkrzywanski.pn.webservice.common;

import java.time.Instant;

public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String serviceName;
    private String path;

    private ErrorResponse() {
    }

    public ErrorResponse(final Instant timestamp, final int status, final String error,
                         final String message, final String serviceName, final String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.serviceName = serviceName;
        this.path = path;
    }

    Instant getTimestamp() {
        return timestamp;
    }

    int getStatus() {
        return status;
    }

    String getError() {
        return error;
    }

    String getMessage() {
        return message;
    }

    String getServiceName() {
        return serviceName;
    }

    String getPath() {
        return path;
    }
}
