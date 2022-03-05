package io.mkrzywanski.keycloak.listeners;

public interface UserServiceClient {
    void notifyUserCreated(UserCreatedEventData eventData);
}
