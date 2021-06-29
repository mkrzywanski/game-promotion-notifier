package io.mkrzywanski.gpn.user;

public class EmailAddress {

    private final String email;

    public EmailAddress(final String email) {
        this.email = email;
    }

    public static EmailAddress of(final String value) {
        return new EmailAddress(value);
    }

    public String asString() {
        return email;
    }
}
