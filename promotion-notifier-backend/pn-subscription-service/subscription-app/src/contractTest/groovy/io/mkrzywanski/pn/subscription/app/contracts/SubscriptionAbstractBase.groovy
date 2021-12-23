package io.mkrzywanski.pn.subscription.app.contracts

import groovy.transform.PackageScope
import io.mkrzywanski.pn.subscription.app.adapters.SubscriptionFacade
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification;

@SpringBootTest(classes = [TestConfig], webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@PackageScope
abstract class SubscriptionAbstractBase extends Specification {
    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected FilterChainProxy springSecurityFilterChain

    @MockBean
    protected SubscriptionFacade subscriptionFacade

    @BeforeEach
    void setup() {
        RestAssuredMockMvc
                .standaloneSetup(MockMvcBuilders.webAppContextSetup(this.context)
                        .addFilter(this.springSecurityFilterChain))


    }

}
