package io.mkrzywanski.pn.email.domain


import io.mkrzywanski.pn.email.api.UserData
import io.mkrzywanski.pn.email.infra.ThymleafConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.not
import static org.hamcrest.Matchers.notNullValue
import static org.hamcrest.text.IsEmptyString.emptyOrNullString

@SpringBootTest(classes = [ThymleafConfig, NewPostsMatchedEmailGenerator])
class NewPostsMatchedEmailGeneratorSpec extends Specification {

    @Autowired
    NewPostsMatchedEmailGenerator newPostsMatchedEmailGenerator

    def "should generate email html content"() {
        given:
        def userData = new UserData("michal", "username")
        def newOffersNotificationData = new NewOffersEmailData(userData, List.of())

        when:
        def emailContent = newPostsMatchedEmailGenerator.generate(newOffersNotificationData)

        then:
        noExceptionThrown()
        emailContent notNullValue()
        with(emailContent) {
            assertThat(content, not(emptyOrNullString()))
        }

    }
}
