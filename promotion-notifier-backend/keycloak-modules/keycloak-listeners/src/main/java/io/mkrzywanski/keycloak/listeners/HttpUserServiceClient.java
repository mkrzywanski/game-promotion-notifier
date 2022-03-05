package io.mkrzywanski.keycloak.listeners;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class HttpUserServiceClient implements UserServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUserServiceClient.class);

    private final String url;
    private final Client resteasyClient;

    HttpUserServiceClient(final String url) {
        this.url = url;
        this.resteasyClient = ResteasyClientBuilder.newBuilder()
                .build();
    }

    @Override
    public void notifyUserCreated(final UserCreatedEventData eventData) {
        LOG.info("Trying to notify user created");
        LOG.info("Url " + url);
        final Response response = resteasyClient.target(url + "/created").request()
                .post(Entity.entity(eventData, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("User service response : " + response.getStatus());
    }

}
