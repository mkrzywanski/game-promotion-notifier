package io.mkrzywanski.gpn.subscription.integ

import io.mkrzywanski.gpn.subscription.app.SubscriptionServiceApp
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName

@Configuration
@Import(SubscriptionServiceApp)
class IntegrationTestConfig {

    public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.13.2"

    @Bean(destroyMethod = "")
    def elasticsearchContainer() {
        def container = new ElasticsearchContainer(DockerImageName.parse(ELASTICSEARCH_IMAGE))
                .withEnv("discovery.type", "single-node")
                .withReuse(true)
        container.start()
        container
    }

    @Bean
    @Primary
    def testElasticsearchClient() throws UnknownHostException {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:${elasticsearchContainer().getFirstMappedPort()}")
                .build();

        return RestClients.create(clientConfiguration).rest()
    }

}
