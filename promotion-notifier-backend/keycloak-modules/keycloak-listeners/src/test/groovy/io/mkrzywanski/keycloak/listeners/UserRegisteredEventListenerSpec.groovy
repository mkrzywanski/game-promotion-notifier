package io.mkrzywanski.keycloak.listeners

import org.keycloak.events.Event
import org.keycloak.events.EventType
import spock.lang.Specification

class UserRegisteredEventListenerSpec extends Specification {


    def userServiceClient = Mock(UserServiceClient)
    def userRegisteredEventListener = new UserRegisteredEventListener(userServiceClient)

    def 'should call user service when register event appears'() {
        given:
        Event event = new Event()
        event.type = EventType.REGISTER
        event.userId = "803ce2d3-7eb3-40dc-991e-d0988722c085"
        event.details = details()

        when:
        userRegisteredEventListener.onEvent(event)

        then:
        1 * userServiceClient.notifyUserCreated(UserCreatedEvent.create(event.details, event.userId))
    }

    def "should do nothing when event is different then register"() {
        given:
        Event event = new Event()
        event.type = EventType.CLIENT_DELETE

        when:
        userRegisteredEventListener.onEvent(event)

        then:
        0 * userServiceClient.notifyUserCreated(_)
    }

    def details() {
        Map.of(
                "username", "user1",
                "first_name", "Michal",
                "last_name", "K",
                "email", "test@test.pl"
        )
    }


}
