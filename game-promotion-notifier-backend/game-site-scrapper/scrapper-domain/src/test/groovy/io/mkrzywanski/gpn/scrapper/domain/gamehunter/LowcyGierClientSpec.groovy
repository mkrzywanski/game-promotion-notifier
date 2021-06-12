package io.mkrzywanski.gpn.scrapper.domain.gamehunter

import com.github.tomakehurst.wiremock.WireMockServer
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.responseDefinition
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class LowcyGierClientSpec extends Specification {

    final WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort())
    def lowcyGierClient

    void setup() {
        wireMockServer.start()
        lowcyGierClient = new LowcyGierClient("http://localhost:%s".formatted(wireMockServer.port()))
    }

    void cleanup() {
        wireMockServer.stop()
    }

    def "should get response"() {
        given:
        def response = responseDefinition().withBody("test").withStatus(200)
        def stub = get(urlEqualTo("/page/1/"))
        wireMockServer.stubFor(stub.willReturn(response))

        when:
        def result = lowcyGierClient.getPage(1)

        then:
        result == 'test'
    }

    def "should fail"() {
        given:
        def response = responseDefinition().withBody("failed").withStatus(500)
        def stub = get(urlEqualTo("/page/1/"))
        wireMockServer.stubFor(stub.willReturn(response))

        when:
        lowcyGierClient.getPage(1)

        then:
        thrown(LowcyGierClientException)
    }
}
