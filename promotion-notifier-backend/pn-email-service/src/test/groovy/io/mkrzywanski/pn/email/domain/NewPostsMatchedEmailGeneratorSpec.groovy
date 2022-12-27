package io.mkrzywanski.pn.email.domain


import io.mkrzywanski.pn.email.api.UserData
import io.mkrzywanski.pn.email.infra.ThymleafConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.not
import static org.hamcrest.Matchers.notNullValue
import static org.hamcrest.text.IsEmptyString.emptyOrNullString

@SpringBootTest
@ContextConfiguration(classes = [NewPostsMatchedEmailGenerator, ThymleafConfig])
class NewPostsMatchedEmailGeneratorSpec extends Specification {

    @Autowired
    NewPostsMatchedEmailGenerator newPostsMatchedEmailGenerator

    def "should generate email html content"() {
        given:
        def userData = new UserData(UUID.fromString("ca8144a4-2040-4fc1-ab4f-28cb9ac404f0"), "username", "michal", "test@tes.pl")
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
