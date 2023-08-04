package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AgeFilterTest {
    @ParameterizedTest
    @MethodSource("filterArguments")
    public void testFilter(long durationInMs, boolean expected, Instant quoteTimestamp) {
        var filter = new AgeFilter(durationInMs);
        var bid = quoteTimestamp != null ? new Quote("EURUSD", "reuters", 1.5, QuoteType.BID, quoteTimestamp.toEpochMilli()) : null;
        assertEquals(expected, filter.filter(bid));
    }

    static Stream<Arguments> filterArguments() {
        var durationInMs = Duration.ofHours(2).toMillis();
        var now = Instant.now();
        return Stream.of(
                arguments(durationInMs, true, now.minusSeconds(1000)),
                arguments(durationInMs, true, now.minusSeconds(3600)),
                arguments(durationInMs, true, now.plusSeconds(3600)), // future
                arguments(durationInMs, false, now.minusSeconds(90000)),
                arguments(durationInMs, true, null)
        );
    }
}
