package io.mkrzywanski.pn.matching.app.contract.user

import io.mkrzywanski.pn.matching.infa.http.RestTemplateConfig
import io.mkrzywanski.pn.matching.user.HttpUserServiceClient
import io.mkrzywanski.pn.matching.user.UserSerivceClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [RestTemplateConfig])
@AutoConfigureStubRunner(ids = "io.mkrzywanski:user-app:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ActiveProfiles("test")
@EnableAutoConfiguration
class UserServiceContractSpec extends Specification {

    public static final UUID userId = UUID.fromString('e083123c-eac4-463d-bc59-7f2e3fa3cbe1')

    @StubRunnerPort("io.mkrzywanski:user-app")
    int producerPort;

    @Autowired
    RestTemplate restTemplate

    UserSerivceClient userServiceClient

    void setup() {
        userServiceClient = new HttpUserServiceClient("http://localhost:${producerPort}", restTemplate)
    }

    def "test"() {
        given:
        when:
        def d = userServiceClient.getUserDetails(userId)
        then:
        with(d) {
            d.userId == userId
            d.username == 'test'
            d.firstName == 'test'
            d.email == 'test@test.pl'
        }
    }
}
//
//@Configuration
//class TestConfig {
//    @Bean
//    UserSerivceClient userSerivceClient() {
//        new HttpUserServiceClient("local")
//    }
//}

