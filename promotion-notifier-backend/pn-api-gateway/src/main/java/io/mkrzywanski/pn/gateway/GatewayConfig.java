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

import java.util.function.Function;

@Configuration
class GatewayConfig {

    @Value("${subscriptions.url}")
    private String subscriptionsUrl;

    @Value("${users.url}")
    private String usersUrl;

    @Autowired
    private TokenRelayGatewayFilterFactory tokenFilterFactory;

    @Bean
    RouteLocator routeLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("/v1/subscriptions", subscriptionsRoute())
                .route("users", usersRoute())
                .build();
    }

    private Function<PredicateSpec, Buildable<Route>> usersRoute() {
        return spec -> spec
                .path("/users/**")
                .filters(s -> s.filter(tokenFilterFactory.apply()))
                .uri(usersUrl);
    }

    private Function<PredicateSpec, Buildable<Route>> subscriptionsRoute() {
        return spec -> spec
                .path("/subscriptions/**")
                .filters(s -> s.filter(tokenFilterFactory.apply()))
                .uri(subscriptionsUrl);
    }

}
