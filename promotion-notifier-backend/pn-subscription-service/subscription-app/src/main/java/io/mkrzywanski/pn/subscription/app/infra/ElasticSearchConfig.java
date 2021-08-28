package io.mkrzywanski.pn.subscription.app.infra;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.RefreshPolicy;

@Configuration
class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private final RestClientBuilder restClientBuilder;

    ElasticSearchConfig(final RestClientBuilder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    protected RefreshPolicy refreshPolicy() {
        return RefreshPolicy.IMMEDIATE;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(restClientBuilder);
    }
}
