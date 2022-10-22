package io.mkrzywanski.pn.matching.infra.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize.mvcMatchers("/actuator/*").permitAll())
                .csrf().disable();
    }
}
