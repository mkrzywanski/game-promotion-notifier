package io.mkrzywanski.gpn.scrapper.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMongoRepositories
public class GameSiteScrapperApp {

    public static void main(final String[] args) {
        SpringApplication.run(GameSiteScrapperApp.class);
    }
}
