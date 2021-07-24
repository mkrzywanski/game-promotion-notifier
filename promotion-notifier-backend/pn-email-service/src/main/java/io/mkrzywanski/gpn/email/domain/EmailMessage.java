package io.mkrzywanski.gpn.email.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EmailMessage {
    EmailContent emailContent;
    EmailAddress target;
    Subject subject;

    public String subject() {
        return subject.getSubject();
    }

    public String emailContent() {
        return emailContent.getContent();
    }

    public String target() {
        return target.getEmail();
    }
}
