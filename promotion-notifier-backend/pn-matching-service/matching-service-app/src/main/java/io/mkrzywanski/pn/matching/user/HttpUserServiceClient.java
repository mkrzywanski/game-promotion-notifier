package io.mkrzywanski.pn.matching.user;

import io.mkrzywanski.pn.matching.infra.http.ClientCommunicationException;
import io.mkrzywanski.pn.webservice.common.error.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class HttpUserServiceClient implements UserSerivceClient {

    private final String url;
    private final WebClient webclient;

    public HttpUserServiceClient(@Value("${gpn.user-service.url}") final String url, final WebClient webclient) {
        this.url = url;
        this.webclient = webclient;
    }

    private static Mono<? extends Throwable> handleError(final ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ErrorResponse.class)
                .flatMap(errorResponse -> Mono.error(new ClientCommunicationException(errorResponse)));
    }

    @Override
    public UserDetails getUserDetails(final UUID userId) {
        final String finalUrl = url + "/v1/users/" + userId;
        return webclient.get()
                .uri(finalUrl)
                .retrieve()
                .onStatus(HttpStatus::isError, HttpUserServiceClient::handleError)
                .bodyToMono(UserDetails.class)
                .block();
    }
}
