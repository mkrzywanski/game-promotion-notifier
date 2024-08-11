package io.mkrzywanski.keycloak.listeners;

public interface UserServiceClient {
    Result notifyUserCreated(UserCreatedEvent eventData);
}
