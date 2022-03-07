package io.mkrzywanski.keycloak.listeners;

public interface UserServiceClient {
    Result notifyUserCreated(UserCreatedEventData eventData);
}
