package io.mkrzywanski.keycloak.listeners;

import org.apache.http.HttpStatus;
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

    //TODO think what to do in case of failure? Should we make retries or maybe use queue instead of HTTP
    @Override
    public Result notifyUserCreated(final UserCreatedEventData eventData) {
        LOG.info("Trying to notify user created");
        LOG.info("Url " + url);
        final Response response = resteasyClient.target(url + "/v1/users").request()
                .post(Entity.entity(eventData, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("User service response : " + response.getStatus());
        if (response.getStatus() == HttpStatus.SC_CREATED) {
            return Result.SUCCESS;
        } else {
            return Result.FAILURE;
        }
    }

}
