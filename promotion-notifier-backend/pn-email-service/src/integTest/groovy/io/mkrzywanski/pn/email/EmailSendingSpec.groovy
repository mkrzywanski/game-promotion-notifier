package io.mkrzywanski.pn.email


import io.mkrzywanski.pn.email.config.IntegrationTestConfig
import io.mkrzywanski.pn.email.domain.Currencies
import io.mkrzywanski.pn.email.domain.Price
import io.mkrzywanski.pn.email.smtp.SmtpServer
import io.mkrzywanski.pn.email.api.NewOffersNotificationData
import io.mkrzywanski.pn.email.api.OfferData
import io.mkrzywanski.pn.email.api.PostData
import io.mkrzywanski.pn.email.api.UserData
import io.mkrzywanski.pn.email.infra.RabbitInfrastructureProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.Callable

import static io.mkrzywanski.pn.email.matchers.MatcherBuilder.*
import static io.mkrzywanski.pn.email.matchers.MimeMessageContentMatcher.*
import static io.mkrzywanski.pn.email.matchers.MimeMessageFromEmailMatcher.isFrom
import static io.mkrzywanski.pn.email.matchers.MimeMessageSubjectMatcher.hasSubject
import static org.awaitility.Awaitility.*
import static org.hamcrest.MatcherAssert.assertThat

@SpringBootTest
@ContextConfiguration(classes = IntegrationTestConfig)
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
        return { -> emailsReceivedCountIs(1) }
    }

    private void dataAppearOnQueue() {
        def userData = new UserData(UUID.fromString("827fba77-f7ad-43a8-9190-de8b10d74568"), "username", "firstName", "test@test.pl")
        def postData = List.of(new PostData("http://test", List.of(new OfferData("Rainbow Six", "http://test.pl", Set.of(Price.of(Currencies.PLN, BigDecimal.ONE))))))
        newOffersNotificationData = new NewOffersNotificationData(userData, postData)
        rabbitTemplate.convertAndSend(rabbitInfrastructureProperties.getExchange(), rabbitInfrastructureProperties.getQueue(), newOffersNotificationData)
        await().atMost(Duration.ofSeconds(10)).until(anyEmailIsReceived())
    }

    boolean emailContentIsValid() {
        def email = smtpServer.getReceivedMessages().first()
        def isValid = matchesEvery(
                hasSubject("New Posts matched!"))
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
