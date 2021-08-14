package io.mkrzywanski.pn.matching.infa.http;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {
    @Bean
    private RestTemplate httpClient(final RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }
}
