package io.mkrzywanski.gpn.user

import spock.lang.Specification

class UserServiceSpec extends Specification {

    def repo = Stub(UserRepository)
    def userService = new UserService(repo)

    def "should save user"() {
        given:
        def user = UserObjectMother.user().build();
        def newUserDetails = new NewUserDetails(user.getUsername(), user.getEmailAddress(), user.getFirstName())
        repo.save(newUserDetails) >> user.getUserId()

        when:
        def actualUserId = userService.create(newUserDetails)

        then:
        actualUserId == user.getUserId()
    }
}
