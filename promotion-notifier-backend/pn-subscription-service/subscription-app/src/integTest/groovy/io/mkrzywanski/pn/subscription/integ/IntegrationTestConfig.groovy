package io.mkrzywanski.pn.subscription.integ

import io.mkrzywanski.pn.subscription.app.SubscriptionServiceApp
import io.mkrzywanski.shared.keycloak.KeyCloakContainer
import io.mkrzywanski.shared.keycloak.KeyCloakProperties
import io.mkrzywanski.shared.keycloak.spring.KeycloakContainerConfiguration
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@Import([SubscriptionServiceApp, KeycloakContainerConfiguration])
class IntegrationTestConfig {

    public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.13.2"

    @Bean(destroyMethod = "")
    ElasticsearchContainer elasticsearchContainer() {
        def container = new ElasticsearchContainer(DockerImageName.parse(ELASTICSEARCH_IMAGE))
                .withEnv("discovery.type", "single-node")
                .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
        container.start()
        container
    }

    @Bean
    @Primary
    RestHighLevelClient testElasticsearchClient() throws UnknownHostException {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:${elasticsearchContainer().getFirstMappedPort()}")
                .build();

        RestClients.create(clientConfiguration).rest()
    }

    @Bean
    SupplierJwtDecoder jwtDecoderByIssuerUri(KeyCloakContainer keyCloakContainer, KeyCloakProperties keyCloakProperties) {
        return new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation("http://localhost:${keyCloakContainer.firstMappedPort}/auth/realms/${keyCloakProperties.testRealm}"));
    }

}
