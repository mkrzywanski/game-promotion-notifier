package io.mkrzywanski.gpn.scrapper.app.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableMongoRepositories
public class SpringConfig {
}
