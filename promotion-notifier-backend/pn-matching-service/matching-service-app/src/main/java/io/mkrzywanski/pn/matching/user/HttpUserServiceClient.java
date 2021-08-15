package io.mkrzywanski.pn.matching.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class HttpUserServiceClient implements UserSerivceClient {

    private final String url;
    private final RestTemplate restTemplate;

    public HttpUserServiceClient(@Value("${gpn.user-service.url}") final String url, final RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails getUserDetails(final UUID userId) {
        final String finalUrl = url + "/v1/users/" + userId;
        return restTemplate.getForEntity(finalUrl, UserDetails.class).getBody();
    }
}
