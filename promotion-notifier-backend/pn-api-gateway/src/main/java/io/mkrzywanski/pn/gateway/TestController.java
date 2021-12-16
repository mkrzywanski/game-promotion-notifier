package io.mkrzywanski.pn.gateway;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
class TestController {
    @GetMapping("/test")
    Mono<String> s(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                   @AuthenticationPrincipal OAuth2User oauth2User) {
        return Mono.just("hello");
    }
}
