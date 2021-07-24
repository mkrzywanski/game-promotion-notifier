package io.mkrzywanski.pn.scrapper.app.infra;

import com.mongodb.ConnectionString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PersistenceConfig {

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private Environment environment;

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
        final String replicaSet = environment.getProperty("mongodb.replicaset");
        final String database = mongoProperties.getDatabase();
        final String host = mongoProperties.getHost();
        final int port = mongoProperties.getPort();
        final String connectionString = String.format("mongodb://%s:%s/%s?replicaSet=%s", host, port, database, replicaSet);
        return settings -> settings.applyConnectionString(new ConnectionString(connectionString));
    }
}
