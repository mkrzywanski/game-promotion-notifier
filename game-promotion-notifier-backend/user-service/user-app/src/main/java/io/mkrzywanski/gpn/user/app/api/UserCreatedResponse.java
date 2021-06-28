package io.mkrzywanski.gpn.user.app.api;

public class UserCreatedResponse {
    private String userId;

    public UserCreatedResponse(final String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
