package io.mkrzywanski.pn.user.app.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"io.mkrzywanski.pn.user.app"})
class Config {

}
