package io.mkrzywanski.gpn.user.app.infra;

import io.mkrzywanski.gpn.user.UserService;
import io.mkrzywanski.gpn.user.app.adapters.persistance.DbUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"io.mkrzywanski.gpn.user.app"})
class Config {

    @Bean
    UserService userService(final DbUserRepository dbUserRepository) {
        return new UserService(dbUserRepository);
    }

}
