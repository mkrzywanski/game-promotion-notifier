package io.mkrzywanski.gpn.scrapper.domain.gamehunter;

import io.mkrzywanski.gpn.scrapper.domain.post.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class LowcyGierPriceParser {

    private static final Pattern PATTERN = Pattern.compile("(\\d+(?:\\.\\d{1,2})?)\\s(([\\€])|(zł))");
    private static final String FREE = "za darmo";

    static GamePrice parse(final String input) {
        if (input.equalsIgnoreCase(FREE)) {
            return new FreeGamePrice();
        }

        final Collection<GamePrice> gamePrices = extractPrices(input);

        if (gamePrices.size() > 1) {
            return new CompositeGamePrice(gamePrices);
        } else if (gamePrices.size() == 1) {
            return gamePrices.stream().findFirst().get();
        } else {
            return new EmptyGamePrice();
        }

    }

    private static Collection<GamePrice> extractPrices(final String input) {
        final Matcher matcher = PATTERN.matcher(input);

        final Set<String> matches = new HashSet<>();
        while (matcher.find()) {
            final String group = matcher.group();
            matches.add(group);
        }

        return matches.stream().map(NumberGamePrice::fromString).collect(Collectors.toList());
    }
}
