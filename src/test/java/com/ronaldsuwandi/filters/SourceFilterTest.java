package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SourceFilterTest {
    @ParameterizedTest
    @MethodSource("filterArguments")
    public void testFilter(String source, FilterResult expected, String bidSource, String askSource) {
        SourceFilter filter = new SourceFilter(source);
        Quote bid = bidSource != null ? new Quote("EURUSD", bidSource, 1.5, QuoteType.BID, 0) : null;
        Quote ask = askSource != null ? new Quote("EURUSD", askSource, 1.5, QuoteType.ASK, 0) : null;
        assertEquals(expected, filter.filter(bid, ask));
    }

    static Stream<Arguments> filterArguments() {
        String source = "bloomberg";
        return Stream.of(
                arguments(source, FilterResult.Success, source, source),
                arguments(source, FilterResult.AskFails, source, "reuters"),
                arguments(source, FilterResult.BidFails, "reuters", source),
                arguments(source, FilterResult.BidAskFails, "reuters", "reuters"),
                arguments(source, FilterResult.Success, source, null),
                arguments(source, FilterResult.Success, null, source),
                arguments(source, FilterResult.Success, null, null),
                arguments(source, FilterResult.BidFails, "reuters", null),
                arguments(source, FilterResult.AskFails, null, "reuters")
        );
    }
}
