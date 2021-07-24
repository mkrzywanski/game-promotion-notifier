package io.mkrzywanski.gpn.email.matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

import javax.mail.internet.MimeMessage

class MimeMessageFromEmailMatcher extends TypeSafeMatcher<MimeMessage>{

    private final String from

    static isFrom(String from) {
        new MimeMessageFromEmailMatcher(from)
    }

    MimeMessageFromEmailMatcher(String from) {
        this.from = from
    }

    @Override
    protected boolean matchesSafely(MimeMessage item) {
        item.from[0].toString() == from
    }

    @Override
    void describeTo(Description description) {

    }
}
