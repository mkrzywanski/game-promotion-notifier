package io.mkrzywanski.keycloak.listeners;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class UserRegisteredEventListener implements EventListenerProvider {

    private static final Logger LOG = LoggerFactory.getLogger(UserRegisteredEventListener.class);

    private final UserServiceClient userServiceClient;

    UserRegisteredEventListener(final UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void onEvent(final Event event) {
        if (EventType.REGISTER == event.getType()) {
            final String userId = event.getUserId();
            final Map<String, String> details = event.getDetails();
            userServiceClient.notifyUserCreated(UserCreatedEventData.create(details, userId));
        }
    }

    @Override
    public void onEvent(final AdminEvent event, final boolean includeRepresentation) {
        LOG.info("ADMIN EVENT");
    }

    @Override
    public void close() {

    }
}
