package io.mkrzywanski.pn.subscription.app.infra;

import io.mkrzywanski.pn.subscription.SubscriptionRepository;
import io.mkrzywanski.pn.subscription.SubscriptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ServiceConfig {
    @Bean
    SubscriptionService subscriptionService(final SubscriptionRepository subscriptionRepository) {
        return new SubscriptionService(subscriptionRepository);
    }
}
