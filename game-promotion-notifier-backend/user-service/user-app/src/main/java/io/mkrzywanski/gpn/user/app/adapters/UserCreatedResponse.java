package io.mkrzywanski.gpn.user.app.adapters;

public class UserCreatedResponse {
    private String userId;

    public UserCreatedResponse(final String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
