package io.mkrzywanski.keycloak.listeners;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class UserRegisteredListenerProviderFactory implements EventListenerProviderFactory {

    private Config.Scope scope;

    public UserRegisteredListenerProviderFactory() {
    }

    @Override
    public EventListenerProvider create(final KeycloakSession session) {
        final String url = UserServicePropertiesProvider.getUrl();
        return new UserRegisteredEventListener(new HttpUserServiceClient(url));
    }

    @Override
    public void init(final Config.Scope scope) {
        this.scope = scope;
    }

    @Override
    public void postInit(final KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "UserRegisteredListenerProviderFactory";
    }

    @Override
    public int order() {
        return EventListenerProviderFactory.super.order();
    }
}
