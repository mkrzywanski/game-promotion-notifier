package io.mkrzywanski.pn.email.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

import jakarta.mail.internet.MimeMessage

class MimeMessageContentMatcher extends TypeSafeMatcher<MimeMessage> {

    private final String expectedContent

    static MimeMessageContentMatcher contentContains(final String content) {
        return new MimeMessageContentMatcher(content)
    }

    MimeMessageContentMatcher(final String expectedContent) {
        this.expectedContent = expectedContent
    }

    @Override
    protected boolean matchesSafely(final MimeMessage item) {
        item.content != null && sanitized(item.content.toString())
                .contains(sanitized(expectedContent))
    }

    private static String sanitized(final String input) {
        input.replaceAll("\\s+", "")
                .replaceAll("\\t+", "")
    }

    @Override
    void describeTo(final Description description) {

    }
}
