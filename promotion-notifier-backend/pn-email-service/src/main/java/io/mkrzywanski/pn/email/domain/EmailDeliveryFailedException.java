package io.mkrzywanski.pn.email.domain;

public class EmailDeliveryFailedException extends Exception {
    EmailDeliveryFailedException(final Throwable cause) {
        super(cause);
    }
}
