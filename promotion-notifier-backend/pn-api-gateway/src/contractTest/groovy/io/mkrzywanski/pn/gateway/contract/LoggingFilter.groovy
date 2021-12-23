package io.mkrzywanski.pn.gateway.contract

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR

@Component
class LoggingFilter implements GlobalFilter {
    Log log = LogFactory.getLog(getClass())

    @Override
    Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        final var uris = exchange.getAttributeOrDefault(GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet())
        final var originalUri = (uris.isEmpty()) ? "Unknown" : uris.iterator().next().toString()
        final Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR)
        final var routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)
        log.info("Incoming request " + originalUri + " is routed to id: " + route.getId()
                + ", uri:" + routeUri)
        return chain.filter(exchange)
    }
}
