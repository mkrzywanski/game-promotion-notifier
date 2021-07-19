package io.mkrzywanski.gpn.user.app

import lombok.AllArgsConstructor
import lombok.Getter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("database")
@AllArgsConstructor
@Getter
class TestDatabaseProperties {
    private String driver
    private String host
    private String username
    private String password
    private String database
}
