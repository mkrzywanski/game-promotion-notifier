package io.mkrzywanski.pn.subscription.app.contracts

import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.KeycloakAccessToken
import com.c4_soft.springaddons.security.oauth2.test.annotations.keycloak.WithMockKeycloakAuth
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionEndpoint
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionFacade
import io.mkrzywanski.pn.subscription.app.api.CreateSubscriptionRequest
import io.mkrzywanski.pn.subscription.app.api.SubscriptionCreatedResponse
import io.mkrzywanski.pn.subscription.app.api.SubscriptionItem
import io.mkrzywanski.pn.subscription.app.infra.SecurityConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.KeycloakDeployment
import org.keycloak.adapters.KeycloakDeploymentBuilder
import org.keycloak.adapters.spi.HttpFacade
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.representations.adapters.config.AdapterConfig
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import java.time.Instant
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;

//@WithMockKeycloakAuth(authorities = ["USER", "AUTHORIZED_PERSONNEL"],
//        claims = @OpenIdClaims(sub = "42",
//                email = "ch4mp@c4-soft.com",
//                emailVerified = true,
//                nickName = "Tonton-Pirate",
//                preferredUsername = "ch4mpy")
//)
abstract class SubscriptionCreateBase extends SubscriptionAbstractBase {

    @BeforeEach
    void setup() {
        def request = new CreateSubscriptionRequest(UUID.fromString("22e90bbd-7399-468a-9b76-cf050ff16c63"), Set.of(new SubscriptionItem("Rainbow Six")))
        Mockito.when(subscriptionFacade.create(Mockito.eq(request))).thenReturn(new SubscriptionCreatedResponse(UUID.fromString("6d692849-58fd-439b-bb2c-50a5d3669fa9")))
    }
}

@KeycloakConfiguration
@Import(KeycloakSpringBootConfigResolver.class)
//    @EnableGlobalMethodSecurity(prePostEnabled = true)
public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            // @formatter:off
            new SecurityConfig().configure(http)
        }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    Object lol(AdapterConfig adapterConfig) {
        println "a"
        new Object()
    }
}

// Work-around https://issues.redhat.com/browse/KEYCLOAK-14520 (versions >=9.0.2 and <11.0.0)
// From 11.0.0 on, @Import(KeycloakSpringBootConfigResolver.class) is enough
// @Configuration
public class SpringBootKeycloakConfigResolver implements KeycloakConfigResolver {

    private KeycloakDeployment keycloakDeployment;

    private AdapterConfig adapterConfig;

    @Autowired
    public SpringBootKeycloakConfigResolver(AdapterConfig adapterConfig) {
        this.adapterConfig = adapterConfig;
    }

    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        if (keycloakDeployment != null) {
            return keycloakDeployment;
        }

        keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig);

        return keycloakDeployment;
    }
}