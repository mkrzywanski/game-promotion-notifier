package io.mkrzywanski.pn.user.app


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

import javax.sql.DataSource

@Configuration
@Import([UserServiceApplication])
//@EnableConfigurationProperties(TestDatabaseProperties.class)
@ConfigurationPropertiesScan
class IntegrationTestConfig {

    private static final POSTGRES_IMAGE_TAG = "13"

    @Autowired
    TestDatabaseProperties dbProps

    @Bean(destroyMethod = "")
    GenericContainer<PostgreSQLContainer> postgreSQLContainer() {
        GenericContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:${POSTGRES_IMAGE_TAG}"))
                .withUsername(dbProps.username)
                .withPassword(dbProps.password)
                .withDatabaseName(dbProps.database)
        postgres.start()
        postgres
    }

    @Bean
    DataSource dataSource(GenericContainer postgreSQLContainer) {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(dbProps.driver)
        dataSourceBuilder.url("jdbc:postgresql://${dbProps.host}:${postgreSQLContainer.firstMappedPort}/${dbProps.database}")
        dataSourceBuilder.username(dbProps.username)
        dataSourceBuilder.password(dbProps.password)
        dataSourceBuilder.build();
    }
}
