package io.mkrzywanski.pn.email.domain


import io.mkrzywanski.pn.email.api.NewOffersNotificationData
import io.mkrzywanski.pn.email.api.OfferData
import io.mkrzywanski.pn.email.api.PostData
import io.mkrzywanski.pn.email.api.UserData
import spock.lang.Specification

class NewPostsMatchedEmailServiceSpec extends Specification {

    def emailSendingService = Mock(EmailSendingService)
    def newPostsMatchedEmailGenerator = Stub(NewPostsMatchedEmailGenerator)
    def newPostsMatchedEmailService = new NewPostsMatchedEmailService(emailSendingService, newPostsMatchedEmailGenerator)


    def "should trigger sending email"() {
        given:

        def targetEmailAddress = "email@email.com"
        def userData = new UserData(UUID.fromString("dd463eee-3425-40b4-b255-a27569e0bc83"), "username", "michal", targetEmailAddress)
        def postData = new PostData("http://link", List.of(new OfferData("rainbow six", "http://test.link", Set.of(Price.of(Currencies.PLN, BigDecimal.ONE)))))
        def postsData = List.of(postData)
        def newOffersNotificationData = new NewOffersNotificationData(userData, postsData)
        def expectedEmailContent = EmailContent.from("dummy content")
        def expectedEmailMessage = new EmailMessage(expectedEmailContent, EmailAddress.of(targetEmailAddress), Subject.of("New Posts matched!"))

        when:
        newPostsMatchedEmailGenerator.generate(new NewOffersEmailData(userData, postsData)) >> expectedEmailContent
        newPostsMatchedEmailService.send(newOffersNotificationData)

        then:
        1 * emailSendingService.send(expectedEmailMessage)
    }
}
