package io.mkrzywanski.pn.matching.infra.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.mvcMatchers("/actuator/*").permitAll())
                .csrf().disable();
        return http.build();
    }
}
