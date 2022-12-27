package io.mkrzywanski.keycloak.listeners


import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

//https://github.com/spockframework/spock/pull/1541
@SpringBootTest(classes = Config)
@AutoConfigureStubRunner(ids = "io.mkrzywanski:pn-user-service:+:stubs", stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
@ContextConfiguration
class UserServiceClientContractSpec extends Specification {

    @StubRunnerPort("io.mkrzywanski:pn-user-service")
    int stubPort

    @RestoreSystemProperties
    def 'should call user service create user'() {
        //given
        def userServiceClient = new HttpUserServiceClient("http://localhost:${stubPort}")
        def userId = "db3ca7f3-7c8b-48ab-8245-4126a1389daf"

        when:
        def result = userServiceClient.notifyUserCreated(UserCreatedEventData.create(getDetails(), userId))

        then:
        result == Result.SUCCESS

    }

    def getDetails() {
        Map.of("username", "user1",
                "first_name", "Michal",
                "last_name", "K",
                "email", "test@test.pl")
    }
}


@Configuration
class Config {

}
