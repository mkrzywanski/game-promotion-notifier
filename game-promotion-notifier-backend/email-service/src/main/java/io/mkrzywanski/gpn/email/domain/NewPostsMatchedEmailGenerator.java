package io.mkrzywanski.gpn.email.domain;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class NewPostsMatchedEmailGenerator {

    private final TemplateEngine templateEngine;

    public NewPostsMatchedEmailGenerator(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public EmailContent generate(final NewOffersEmailData newOffersEmailData) {
        final Context context = new Context();
        context.setVariable("user", newOffersEmailData.getUserData());
        final String html = templateEngine.process("message", context);
        return EmailContent.from(html);
    }
}
