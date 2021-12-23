package io.mkrzywanski.pn.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.function.Function;

@Configuration
public class GatewayConfig {

    @Value("${subscriptions.url}")
    protected String subscriptionsUrl;

    @Value("${users.url}")
    protected String usersUrl;

    @Autowired
    private TokenRelayGatewayFilterFactory tokenFilterFactory;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    RouteLocator routeLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("ignore private", ignore())
                .route("subscriptions", subscriptionsRoute())
                .route("users", usersRoute())
                .build();
    }

    private Function<PredicateSpec, Buildable<Route>> ignore() {
        return predicateSpec -> predicateSpec.path("subscriptions/match")
                .filters(subscriptionsUrl -> subscriptionsUrl.setStatus(HttpStatus.NOT_FOUND.value()))
                .uri("no://op");
    }

    private Function<PredicateSpec, Buildable<Route>> usersRoute() {
        return spec -> spec
                .path("/users/**")
                .filters(s -> s.filter(tokenFilterFactory.apply()).prefixPath("/v1"))
                .uri(usersUrl);
    }

    private Function<PredicateSpec, Buildable<Route>> subscriptionsRoute() {
        return spec -> spec
                .path("/subscriptions/**")
                .filters(s -> s.filter(tokenFilterFactory.apply()).prefixPath("/v1"))
                .uri(subscriptionsUrl);
    }

}
