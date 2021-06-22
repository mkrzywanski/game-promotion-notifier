package io.mkrzywanski.gpn.user.app.infra;

import io.mkrzywanski.gpn.user.UserService;
import io.mkrzywanski.gpn.user.app.adapters.DbUserRepository;
import io.mkrzywanski.gpn.user.app.adapters.JpaUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public UserService userService(final JpaUserRepository jpaUserRepository) {
        return new UserService(new DbUserRepository(jpaUserRepository));
    }

}
