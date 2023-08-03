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
    public void testFilter(long durationInMs, FilterResult expected, Instant bidTimestamp, Instant askTimestamp) {
        AgeFilter filter = new AgeFilter(durationInMs);

        Quote bid = bidTimestamp != null ? new Quote("EURUSD", "reuters", 1.5, QuoteType.BID, bidTimestamp.toEpochMilli()) : null;
        Quote ask = askTimestamp != null ? new Quote("EURUSD", "reuters", 1.5, QuoteType.ASK, askTimestamp.toEpochMilli()) : null;

        assertEquals(expected, filter.filter(bid, ask));
    }

    static Stream<Arguments> filterArguments() {
        long durationInMs = Duration.ofHours(2).toMillis();
        Instant now = Instant.now();
        return Stream.of(
                arguments(durationInMs, FilterResult.Success, now.minusSeconds(1000), now.minusSeconds(1000)),
                arguments(durationInMs, FilterResult.Success, now.minusSeconds(3600), now.minusSeconds(3600)),
                arguments(durationInMs, FilterResult.Success, now.plusSeconds(3600), now.plusSeconds(3600)), // future
                arguments(durationInMs, FilterResult.BidFails, now.minusSeconds(90000), now.minusSeconds(1000)),
                arguments(durationInMs, FilterResult.AskFails, now.minusSeconds(1000), now.minusSeconds(90000)),
                arguments(durationInMs, FilterResult.BidAskFails, now.minusSeconds(90000), now.minusSeconds(90000)),
                arguments(durationInMs, FilterResult.Success, null, null),
                arguments(durationInMs, FilterResult.Success, now.minusSeconds(1000), null),
                arguments(durationInMs, FilterResult.Success, null, now.minusSeconds(1000)),
                arguments(durationInMs, FilterResult.AskFails, null, now.minusSeconds(90000)),
                arguments(durationInMs, FilterResult.BidFails, now.minusSeconds(90000), null)
        );
    }
}
