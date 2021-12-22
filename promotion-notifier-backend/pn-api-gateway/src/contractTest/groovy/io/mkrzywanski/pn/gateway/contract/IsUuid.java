package io.mkrzywanski.pn.gateway.contract;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.UUID;

public class IsUuid extends BaseMatcher<CharSequence> {

    public static Matcher<CharSequence> isUUID() {
        return new IsUuid();
    }

    @Override
    public boolean matches(final Object arg) {
        try {
            UUID.fromString(arg.toString());
        } catch (final IllegalArgumentException ex) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("valid UUID format");
    }

}
