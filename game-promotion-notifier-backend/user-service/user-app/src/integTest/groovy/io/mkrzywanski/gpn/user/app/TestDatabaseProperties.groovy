package io.mkrzywanski.gpn.user.app


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("database")
class TestDatabaseProperties {
    private String driver
    private String host
    private String username
    private String password
    private String database

    TestDatabaseProperties(String driver, String host, String username, String password, String database) {
        this.driver = driver
        this.host = host
        this.username = username
        this.password = password
        this.database = database
    }

    String getDriver() {
        return driver
    }

    String getHost() {
        return host
    }

    String getUsername() {
        return username
    }

    String getPassword() {
        return password
    }

    String getDatabase() {
        return database
    }
}
