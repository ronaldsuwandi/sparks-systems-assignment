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
    public void testFilter(String source, boolean expected, String bidSource) {
        var filter = new SourceFilter(source);
        var bid = bidSource != null ? new Quote("EURUSD", bidSource, 1.5, QuoteType.BID, 0) : null;
        assertEquals(expected, filter.filter(bid));
    }

    static Stream<Arguments> filterArguments() {
        var source = "bloomberg";
        return Stream.of(
                arguments(source, true, source),
                arguments(source, false, "reuters"),
                arguments(source, false, null)
        );
    }
}
