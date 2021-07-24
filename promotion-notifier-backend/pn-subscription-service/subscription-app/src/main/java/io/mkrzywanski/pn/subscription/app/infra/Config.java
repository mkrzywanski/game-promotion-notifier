package io.mkrzywanski.pn.subscription.app.infra;

import io.mkrzywanski.pn.subscription.SubscriptionRepository;
import io.mkrzywanski.pn.subscription.SubscriptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Configuration
class Config {

    @Bean
    SubscriptionService subscriptionService(final SubscriptionRepository subscriptionRepository) {
        return new SubscriptionService(subscriptionRepository);
    }

    @Bean
    SubscriptionRepository subscriptionRepository(final ElasticsearchOperations elasticsearchOperations) {
        return new io.mkrzywanski.pn.subscription.app.adapters.SubscriptionRepository(elasticsearchOperations, "subscriptions");
    }
}
