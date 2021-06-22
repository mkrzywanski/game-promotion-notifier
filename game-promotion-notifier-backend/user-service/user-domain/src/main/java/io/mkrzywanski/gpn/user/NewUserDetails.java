package io.mkrzywanski.gpn.user;

public class NewUserDetails {

    private final String userName;
    private final EmailAddress email;
    private final String firstName;

    public NewUserDetails(final String userName, final EmailAddress email, final String firstName) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }
}
