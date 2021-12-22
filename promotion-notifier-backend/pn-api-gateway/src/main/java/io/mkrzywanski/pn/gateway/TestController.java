package io.mkrzywanski.pn.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    Mono<String> s(@RegisteredOAuth2AuthorizedClient final OAuth2AuthorizedClient authorizedClient,
                   @AuthenticationPrincipal final OAuth2User oauth2User) {
        LOGGER.info(authorizedClient.getPrincipalName());
        LOGGER.info(oauth2User.getName());
        oauth2User.getAttributes().get("subject");
        return Mono.just("hello");
    }
}
