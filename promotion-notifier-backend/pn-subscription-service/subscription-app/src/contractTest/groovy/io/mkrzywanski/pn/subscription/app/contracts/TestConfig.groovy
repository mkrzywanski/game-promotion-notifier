package io.mkrzywanski.pn.subscription.app.contracts

import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionEndpoint
import io.mkrzywanski.pn.subscription.app.infra.SecurityConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtException

import java.time.Instant

@Configuration
@Import([SubscriptionEndpoint, SecurityConfig])
@EnableAutoConfiguration
class TestConfig {
    @Bean
    @ConditionalOnMissingBean
    JwtDecoder jwtDecoder() {
        JwtDecoder jwtDecoder = new JwtDecoder() {
            @Override
            Jwt decode(final String token) throws JwtException {
                return new Jwt("aaa", Instant.now(), Instant.now().plusSeconds(10), Map.of("alg", "none"), Map.of(JwtClaimNames.SUB, "testUser"))
            }
        }

        return jwtDecoder;
    }

}
