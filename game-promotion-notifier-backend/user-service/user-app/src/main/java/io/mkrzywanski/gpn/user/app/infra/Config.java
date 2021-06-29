package io.mkrzywanski.gpn.user.app.infra;

import io.mkrzywanski.gpn.user.UserService;
import io.mkrzywanski.gpn.user.app.adapters.DbUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {

    @Bean
    UserService userService(final DbUserRepository dbUserRepository) {
        return new UserService(dbUserRepository);
    }

}
