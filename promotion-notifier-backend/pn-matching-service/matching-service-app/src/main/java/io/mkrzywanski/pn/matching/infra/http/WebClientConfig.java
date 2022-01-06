package io.mkrzywanski.pn.matching.infra.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
class WebClientConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebClient.class);

    @Bean
    WebClient webClient(final ClientRegistrationRepository clientRegistrations) {
        final var inMemoryOAuth2AuthorizedClientService = new InMemoryOAuth2AuthorizedClientService(clientRegistrations);
        final var authorizedClientServiceOAuth2AuthorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrations, inMemoryOAuth2AuthorizedClientService);
        final var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientServiceOAuth2AuthorizedClientManager);
        // (optional) explicitly opt into using the oauth2Login to provide an access token implicitly
//        oauth.setDefaultOAuth2AuthorizedClient(true);
        // (optional) set a default ClientRegistration.registrationId
        oauth.setDefaultClientRegistrationId("pn-matching-service");
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(oauth)
                .filter(logRequest())
                .build();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOG.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> LOG.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

}
