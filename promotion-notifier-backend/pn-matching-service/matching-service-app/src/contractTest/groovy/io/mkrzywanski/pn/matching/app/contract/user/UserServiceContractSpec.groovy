package io.mkrzywanski.pn.matching.app.contract.user

import io.mkrzywanski.pn.matching.infa.http.ClientCommunicationException
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

    public static final UUID EXISTING_USER_ID = UUID.fromString('e083123c-eac4-463d-bc59-7f2e3fa3cbe1')
    public static final UUID NON_EXISTING_USER = UUID.fromString("f20848bf-5500-4002-8222-e9fc2dcab6e6")

    @StubRunnerPort("io.mkrzywanski:user-app")
    int producerPort;

    @Autowired
    RestTemplate restTemplate

    UserSerivceClient userServiceClient

    void setup() {
        userServiceClient = new HttpUserServiceClient("http://localhost:${producerPort}", restTemplate)
    }

    def 'should get user details'() {
        given:
        when:
        def userDetails = userServiceClient.getUserDetails(EXISTING_USER_ID)
        then:
        with(userDetails) {
            it.userId == userId
            it.username == 'test'
            it.firstName == 'test'
            it.email == 'test@test.pl'
        }
    }

    def "should throw exception when user could not be found"() {
        given:

        when:
        userServiceClient.getUserDetails(NON_EXISTING_USER)

        then:
        def e = thrown(ClientCommunicationException)
        with(e.errorResponse) {
            it.status == 404
            it.path == "/v1/users/${NON_EXISTING_USER}"
            it.serviceName == 'user-service'
            it.timestamp != null
        }
    }
}

