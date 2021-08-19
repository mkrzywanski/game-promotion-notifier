package io.mkrzywanski.pn.webservice.common.error;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class ErrorResponse {

    //    @Getter(onMethod_ = @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC"))
    private Instant timestamp;
    private int status;
    private String message;
    private String serviceName;
    private String path;

    private ErrorResponse() {
    }

    public ErrorResponse(final Instant timestamp, final int status,
                         final String message, final String serviceName, final String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.serviceName = serviceName;
        this.path = path;
    }

}
