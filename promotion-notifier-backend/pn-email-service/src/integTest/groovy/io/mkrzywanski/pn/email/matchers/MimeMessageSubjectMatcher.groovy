package io.mkrzywanski.pn.email.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

import javax.mail.internet.MimeMessage

class MimeMessageSubjectMatcher extends TypeSafeMatcher<MimeMessage> {

    private final String subject

    static MimeMessageSubjectMatcher hasSubject(final String subject) {
        return new MimeMessageSubjectMatcher(subject)
    }

    MimeMessageSubjectMatcher(final String subject) {
        this.subject = subject
    }

    @Override
    protected boolean matchesSafely(final MimeMessage item) {
        return item.subject == subject
    }

    @Override
    void describeMismatchSafely(final MimeMessage item, final Description mismatchDescription) {
        mismatchDescription.appendValue(subject).appendText(" is not ").appendValue(item.subject)
    }

    @Override
    void describeTo(final Description description) {
        description.appendText("hasSubject(").appendValue(subject).appendText(")");
    }
}
