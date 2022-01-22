package io.mkrzywanski.pn.email.domain;

import io.mkrzywanski.pn.email.api.NewOffersNotificationData;
import org.springframework.stereotype.Component;

@Component
public class NewPostsMatchedEmailService {

    private final EmailSendingService emailSendingService;
    private final NewPostsMatchedEmailGenerator newPostsMatchedEmailGenerator;

    public NewPostsMatchedEmailService(final EmailSendingService emailSendingService, final NewPostsMatchedEmailGenerator newPostsMatchedEmailGenerator) {
        this.emailSendingService = emailSendingService;
        this.newPostsMatchedEmailGenerator = newPostsMatchedEmailGenerator;
    }

    public void send(final NewOffersNotificationData newOffersNotificationData) {
        final NewOffersEmailData newOffersEmailData = new NewOffersEmailData(newOffersNotificationData.getUserData(), newOffersNotificationData.getPostNotificationData());
        final EmailContent emailContent = newPostsMatchedEmailGenerator.generate(newOffersEmailData);
        final EmailMessage emailMessage = EmailMessage.builder()
                .emailContent(emailContent)
                .target(EmailAddress.of(newOffersNotificationData.getUserData().getEmail()))
                .subject(Subject.of("New Posts matched!"))
                .build();
        try {
            emailSendingService.send(emailMessage);
        } catch (final EmailDeliveryFailedException e) {
            e.printStackTrace();
            //implement retry
        }
    }
}
