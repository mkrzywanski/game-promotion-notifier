package io.mkrzywanski.gpn.email.domain

import io.mkrzywanski.gpn.email.api.NewOffersNotificationData
import io.mkrzywanski.gpn.email.api.OfferData
import io.mkrzywanski.gpn.email.api.PostData
import io.mkrzywanski.gpn.email.api.UserData
import spock.lang.Specification

class NewPostsMatchedEmailServiceSpec extends Specification {

    def emailSendingService = Mock(EmailSendingService)
    def newPostsMatchedEmailGenerator = Stub(NewPostsMatchedEmailGenerator)
    def newPostsMatchedEmailService = new NewPostsMatchedEmailService(emailSendingService, newPostsMatchedEmailGenerator)


    def "should trigger sending email"() {
        given:
        def userData = new UserData("michal", "username")
        def postData = new PostData(List.of(new OfferData("rainbow six", Set.of(Price.of(Currencies.PLN, BigDecimal.ONE)))))
        def postsData = List.of(postData)
        def targetEmailAddress = "email@email.com"
        def newOffersNotificationData = new NewOffersNotificationData(userData, targetEmailAddress, postsData)
        def expectedEmailContent = EmailContent.from("dummy content")
        def expectedEmailMessage = new EmailMessage(expectedEmailContent, EmailAddress.of(targetEmailAddress), Subject.of("New Posts matched!"))

        when:
        newPostsMatchedEmailGenerator.generate(new NewOffersEmailData(userData, postsData)) >> expectedEmailContent
        newPostsMatchedEmailService.send(newOffersNotificationData)

        then:
        1 * emailSendingService.send(expectedEmailMessage)
    }
}
