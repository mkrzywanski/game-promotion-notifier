package io.mkrzywanski.gpn.user.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UserServiceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
