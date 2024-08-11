package io.mkrzywanski.pn.subscription.integ

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.mkrzywanski.pn.subscription.app.SubscriptionServiceApp

import io.mkrzywanski.shared.keycloak.KeyCloakProperties
import io.mkrzywanski.shared.keycloak.spring.KeycloakContainerConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.RefreshPolicy
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

@Configuration
@Import([SubscriptionServiceApp, KeycloakContainerConfiguration])
class IntegrationTestConfig extends ElasticsearchConfiguration {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.5.3"

    @Bean(destroyMethod = "")
    ElasticsearchContainer elasticsearchContainer() {
        def container = new ElasticsearchContainer(DockerImageName.parse(ELASTICSEARCH_IMAGE))
                .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
        container.getEnvMap().remove("xpack.security.enabled");
        container.start()
        container
    }

    @Override
    protected RefreshPolicy refreshPolicy() {
        return RefreshPolicy.IMMEDIATE
    }

    @Bean
    SupplierJwtDecoder jwtDecoderByIssuerUri(KeycloakContainer keyCloakContainer, KeyCloakProperties keyCloakProperties) {
        return new SupplierJwtDecoder(() -> JwtDecoders.fromIssuerLocation("http://localhost:${keyCloakContainer.firstMappedPort}/realms/${keyCloakProperties.testRealm}"))
    }

    @Override
    ClientConfiguration clientConfiguration() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:${elasticsearchContainer().getMappedPort(9200)}")
                .usingSsl(getElasticSearchConnectionCertificate(elasticsearchContainer()))
                .withBasicAuth("elastic", "changeme")
                .build();
        clientConfiguration
    }

    private SSLContext getElasticSearchConnectionCertificate(ElasticsearchContainer elasticsearchContainer) {
        byte[] certAsBytes = elasticsearchContainer.copyFileFromContainer(
                "/usr/share/elasticsearch/config/certs/http_ca.crt",
                InputStream::readAllBytes);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        try (InputStream certificateInputStream = new ByteArrayInputStream(certAsBytes)) {
            ca = cf.generateCertificate(certificateInputStream);
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);
        context

    }
}
