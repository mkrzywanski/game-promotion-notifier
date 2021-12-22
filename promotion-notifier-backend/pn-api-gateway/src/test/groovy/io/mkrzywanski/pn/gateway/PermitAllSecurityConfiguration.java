package io.mkrzywanski.pn.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class PermitAllSecurityConfiguration {

    @Bean
    SecurityWebFilterChain springWebFilterChain(final ServerHttpSecurity http)
            throws Exception {
        return http.authorizeExchange().anyExchange().permitAll().and().csrf().disable()
                .build();
    }

}
