package io.mkrzywanski.gpn.user.app

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("database")
class TestDatabaseProperties {

    private String driver
    private String host
    private String username
    private String password
    private String database

    void setDriver(String driver) {
        this.driver = driver
    }

    void setHost(String host) {
        this.host = host
    }

    void setUsername(String username) {
        this.username = username
    }

    void setPassword(String password) {
        this.password = password
    }

    void setDatabase(String database) {
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
