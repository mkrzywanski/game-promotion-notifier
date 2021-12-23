package io.mkrzywanski.pn.gateway.contract

import io.mkrzywanski.pn.gateway.GatewayConfig
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Import
import org.springframework.core.env.ConfigurableEnvironment

@DependsOn("batchStubRunner")
@EnableAutoConfiguration
@Import(LoggingFilter.class)
class GatewayTestConfig extends GatewayConfig implements InitializingBean {

    @Autowired
    private ConfigurableEnvironment environment

    @Override
    void afterPropertiesSet() {
        this.subscriptionsUrl = "http://localhost:${environment.getProperty("stubrunner.runningstubs.io.mkrzywanski.subscription-app.port")}"
    }
}
