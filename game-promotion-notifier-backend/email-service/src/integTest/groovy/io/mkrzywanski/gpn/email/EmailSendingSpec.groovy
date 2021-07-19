package io.mkrzywanski.gpn.email

import io.mkrzywanski.gpn.email.api.NewOffersNotificationData
import io.mkrzywanski.gpn.email.api.OfferData
import io.mkrzywanski.gpn.email.api.PostData
import io.mkrzywanski.gpn.email.api.UserData
import io.mkrzywanski.gpn.email.config.IntegrationTestConfig
import io.mkrzywanski.gpn.email.domain.Currencies
import io.mkrzywanski.gpn.email.domain.Price
import io.mkrzywanski.gpn.email.infra.RabbitInfrastructureProperties
import io.mkrzywanski.gpn.email.matchers.MimeMessageContentMatcher
import io.mkrzywanski.gpn.email.smtp.SmtpServer
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.Callable

import static io.mkrzywanski.gpn.email.matchers.MatcherBuilder.*
import static io.mkrzywanski.gpn.email.matchers.MimeMessageContentMatcher.*
import static io.mkrzywanski.gpn.email.matchers.MimeMessageFromEmailMatcher.isFrom
import static io.mkrzywanski.gpn.email.matchers.MimeMessageSubjectMatcher.hasSubject
import static org.awaitility.Awaitility.*
import static org.hamcrest.MatcherAssert.assertThat

@SpringBootTest(classes = IntegrationTestConfig)
class EmailSendingSpec extends Specification {

    @Autowired
    RabbitTemplate rabbitTemplate

    @Autowired
    RabbitInfrastructureProperties rabbitInfrastructureProperties

    @Autowired
    SmtpServer smtpServer

    @Autowired
    MailProperties mailProperties

    NewOffersNotificationData newOffersNotificationData

    def "should send email"() {

        when:
        dataAppearOnQueue()

        then:
        emailsReceivedCountIs(1)
        emailContentIsValid()
    }

    private boolean emailsReceivedCountIs(int messageCount) {
        smtpServer.getReceivedMessages().size() == messageCount
    }

    private Callable<Boolean> anyEmailIsReceived() {
        return () -> emailsReceivedCountIs(1)
    }

    private void dataAppearOnQueue() {
        UserData userData = new UserData("aaa", "bbb")
        List<PostData> postData = List.of(new PostData(List.of(new OfferData("aaa", Set.of(Price.of(Currencies.PLN, BigDecimal.ONE))))))
        newOffersNotificationData = new NewOffersNotificationData(userData, "test@test.com.pl", postData)
        rabbitTemplate.convertAndSend(rabbitInfrastructureProperties.getExchange(), rabbitInfrastructureProperties.getQueue(), newOffersNotificationData)
        await().atMost(Duration.ofSeconds(10)).until(anyEmailIsReceived())
    }

    boolean emailContentIsValid() {
        def email = smtpServer.getReceivedMessages().first()
        def isValid = matchesEvery(hasSubject("New Posts matched!"))
                .and(isFrom(mailProperties.getUsername()))
                .and(contentContains(
                        """
                    <p>Hello Dear ${newOffersNotificationData.userData.username}</p>
                    <p>There are new hot offers for products that you might be looking for</p>
                    <p>
                        Regards, <br />
                        Promotion notifier
                    </p>
                """))
                .matcher()
        assertThat(email, isValid)
        true
    }
}
