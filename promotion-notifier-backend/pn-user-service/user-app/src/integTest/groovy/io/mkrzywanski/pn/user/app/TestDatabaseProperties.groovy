package io.mkrzywanski.pn.user.app

import groovy.transform.TupleConstructor
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("database")
class TestDatabaseProperties {
    String driver
    String host
    String username
    String password
    String database

    TestDatabaseProperties(driver, host, username, password, database) {
        this.driver = driver
        this.host = host
        this.username = username
        this.password = password
        this.database = database
    }

}
