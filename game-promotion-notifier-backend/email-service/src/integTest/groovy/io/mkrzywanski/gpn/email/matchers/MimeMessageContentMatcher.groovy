package io.mkrzywanski.gpn.email.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

import javax.mail.internet.MimeMessage

class MimeMessageContentMatcher extends TypeSafeMatcher<MimeMessage> {

    private final String expectedContent

    static MimeMessageContentMatcher contentContains(String content) {
        return new MimeMessageContentMatcher(content)
    }

    MimeMessageContentMatcher(final String expectedContent) {
        this.expectedContent = expectedContent
    }

    @Override
    protected boolean matchesSafely(MimeMessage item) {
        item.content != null && sanitized(item.content.toString())
                .contains(sanitized(expectedContent))
    }

    private static String sanitized(String input) {
        input.replaceAll("\\s+", "")
                .replaceAll("\\t+", "")
    }

    @Override
    void describeTo(Description description) {

    }
}
