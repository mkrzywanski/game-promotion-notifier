package io.mkrzywanski.pn.gateway

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

@SpringBootTest
@Import(PermitAllSecurityConfiguration)
class GatewayTest extends Specification{

    @Shared
    WireMockServer wireMockServer

    void setupSpec() {
        wireMockServer = new WireMockServer(options().dynamicPort())
    }

    void cleanupSpec() {
        wireMockServer.stop()
    }

    def "should route to subscription service"() {
//        wireMockServer.
    }
}
