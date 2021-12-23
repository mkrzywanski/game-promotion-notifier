package io.mkrzywanski.pn.gateway.contract

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [GatewayTestConfig.class])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:subscription-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
class SubscriptionSpec {

    private WebTestClient webClient

    @LocalServerPort
    private int port

    @BeforeEach
    void setup() {
        final var baseUri = "http://localhost:" + port
        this.webClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10))
                .baseUrl(baseUri).build()
    }

    @Test
    void shouldRouteToSubscriptions() {

        final var body = "{\"userId\":\"22e90bbd-7399-468a-9b76-cf050ff16c63\",\"itemSet\":[{\"value\":\"Rainbow Six\"}]}"
        webClient.post()
                .uri("/subscriptions")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJERmJmZWVpR29ySXMyZ1VUaDA2d2hfZVZKTUc2azU5X0dVZzBrOVRLb05vIn0.eyJqdGkiOiJlZGY2OGQ2ZS03NjQzLTRjYjgtODU1NS0xOGNkM2M3Njc2ZTIiLCJleHAiOjIwMzA3ODA5MTMsIm5iZiI6MCwiaWF0IjoxNTU3ODI3MzEzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3NjUvYXV0aC9yZWFsbXMvc3ByaW5nX2Nsb3VkX2NvbnRyYWN0cyIsImF1ZCI6InNwcmluZ19jbG91ZF9jb250cmFjdHMiLCJzdWIiOiJjMzNhMzgxNi1lYWExLTRhYWMtYjdhMi1jNDc3NDRkODcwMDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdfY2xvdWRfY29udHJhY3RzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiOGM4ZThhMmMtZDQ5ZC00ZTYxLTlkNTYtOTBkZDVjYTQ1ZDNiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjdXN0b21lciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwic2NvcGUiOiIiLCJhdWQiOiJvYXV0aDItcmVzb3VyY2UiLCJ1c2VyX25hbWUiOiJvbGQgZW5vdWdoIiwidXNlcl9kZXRhaWxzIjp7ImFnZSI6NDIsInVzZXJuYW1lIjoib2xkIGVub3VnaCJ9LCJhdXRob3JpdGllcyI6WyJjdXN0b21lciJdfQ.igcj_dWdpUEr8WPG06yWUde5bluJMujTdefg24R0XQji5EVoEIQV4xT3D0xOtJDOgK0qSCcz5qUi3xsxbvTJp1xD9WXWIl8lFQA0cP4znSdBEYE-Nv9mLgUaF7QfBpL9_hZtYmeNfkvWk6PqOtvh2VlJ1-5esJ5bzUA3s1h0B8wGKWQUOW3-kCV40iX9gb4BIJfxVDnSytzHQO5iRblHpnWYvJJtWJp2Xx91q22xnvQpBqKaF4n3obYae686apMVMpTFgoFwqcNBaBwStImyh9c_kZMZ8ns-eHFcuzmU_ZA9_VNSK2X6vFcG54H3N3Enf8Dz3RX7LM-Q9iziA82COA")
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath('$.subscriptionId').exists()
                .jsonPath('$.subscriptionId').value(IsUuid.isUUID())
    }
}
