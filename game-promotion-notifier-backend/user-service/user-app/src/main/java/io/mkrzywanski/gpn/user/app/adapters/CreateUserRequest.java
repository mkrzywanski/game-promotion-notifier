package io.mkrzywanski.gpn.user.app.adapters;

public class CreateUserRequest {

    private final String firstName;
    private final String userName;
    private final String email;

    public CreateUserRequest(final String firstName, final String userName, final String email) {
        this.firstName = firstName;
        this.userName = userName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserName() {
        return userName;
    }
}
