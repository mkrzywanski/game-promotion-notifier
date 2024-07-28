package io.mkrzywanski.keycloak.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpUserServiceClient implements UserServiceClient {

    private static final Logger LOG = Logger.getLogger(HttpUserServiceClient.class);

    private final ObjectMapper objectMapper;
    private final String url;
    private final HttpClient client;

    HttpUserServiceClient(final String url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    //TODO think what to do in case of failure? Should we make retries or maybe use queue instead of HTTP
    @Override
    public Result notifyUserCreated(final UserCreatedEventData eventData) {
        LOG.info("Trying to notify user created");
        LOG.info("Url " + url);

        String json = toJson(eventData);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .uri(URI.create(url + "/v1/users"))
                .build();
        try {
            HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());
            return send.statusCode() == 201 ? Result.SUCCESS : Result.FAILURE;
        } catch (IOException | InterruptedException e) {
            return Result.FAILURE;
        }
    }

    private String toJson(final UserCreatedEventData eventData) {
        try {
            return objectMapper.writeValueAsString(eventData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
