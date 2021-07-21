package io.mkrzywanski.gpn.email.matchers


import org.hamcrest.Matcher

import static org.hamcrest.Matchers.allOf

class MatcherBuilder<T> {

    private final List<Matcher<T>> matchers

    MatcherBuilder() {
        this.matchers = new ArrayList<>()
    }

    static <T> MatcherBuilder<T> matchesEvery(final Matcher<T> matcher) {
        new MatcherBuilder<T>() & matcher
    }

    MatcherBuilder<T> and(final Matcher<T> matcher) {
        matchers.add(matcher)
        return this
    }

    Matcher<T> matcher() {
        return allOf(matchers.toArray(Matcher[]::new))
    }
}
