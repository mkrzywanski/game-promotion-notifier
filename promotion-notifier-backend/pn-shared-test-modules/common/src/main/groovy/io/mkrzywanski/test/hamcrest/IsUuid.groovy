package io.mkrzywanski.test.hamcrest

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class IsUuid extends BaseMatcher<CharSequence> {

    static Matcher<CharSequence> isUUID() {
        return new IsUuid()
    }

    @Override
    boolean matches(final Object arg) {
        try {
            UUID.fromString(arg.toString())
        } catch (final IllegalArgumentException e) {
            return false
        }
        return true
    }

    @Override
    void describeTo(final Description description) {
        description.appendText("valid UUID format")
    }

}
