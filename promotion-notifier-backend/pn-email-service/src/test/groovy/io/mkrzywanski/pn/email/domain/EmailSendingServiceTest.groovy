package io.mkrzywanski.pn.email.domain


import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import spock.lang.Specification

import jakarta.mail.internet.MimeMessage

class EmailSendingServiceTest extends Specification {

    def javaMailSender = Mock(JavaMailSender)
    def sender = EmailAddress.of("test@test.pl")
    def emailSendingService = new EmailSendingService(javaMailSender, sender)

    def 'should invoke email sending'() {
        given:
        def emailMessage = new EmailMessage(EmailContent.from("content"), EmailAddress.of("target@test.pl"), Subject.of("test subject"))
        javaMailSender.createMimeMessage() >> Mock(MimeMessage)

        when:
        emailSendingService.send(emailMessage)

        then:
        1 * javaMailSender.send(_)
    }

    def "should throw exception if sending email fails"() {
        given:
        def emailMessage = new EmailMessage(EmailContent.from("content"), EmailAddress.of("target@test.pl"), Subject.of("test subject"))
        javaMailSender.createMimeMessage() >> Mock(MimeMessage)
        javaMailSender.send(_ as MimeMessage) >> { _ ->
            throw new MailSendException("error")
        }
        when:
        emailSendingService.send(emailMessage)

        then:
        thrown(EmailDeliveryFailedException)
    }
}
