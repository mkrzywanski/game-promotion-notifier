package io.mkrzywanski.pn.user.app

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "database")
class TestDatabaseProperties {
    String driver
    String host
    String username
    String password
    String database
}
