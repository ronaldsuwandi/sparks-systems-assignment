package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import com.ronaldsuwandi.SymbolQuotes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OutlierFilterTest {
    @ParameterizedTest
    @MethodSource("filterArguments")
    public void testFilter(double targetZScore, boolean expected,
                           Double bidQuote, double bidAverage, double bidStdDev) {
        var quotes = mock(SymbolQuotes.class);
        when(quotes.getQuoteAverage(QuoteType.BID)).thenReturn(bidAverage);
        when(quotes.getQuoteStdDev(QuoteType.BID)).thenReturn(bidStdDev);

        var filter = new OutlierFilter(Map.of("EURUSD", quotes), targetZScore);
        var bid = bidQuote != null ? new Quote("EURUSD", "reuters", bidQuote, QuoteType.BID, 0) : null;
        assertEquals(expected, filter.filter(bid));
    }

    static Stream<Arguments> filterArguments() {
        var targetZScore = 2.0;
        return Stream.of(
                arguments(targetZScore, true, 1.0, 1.5, 2.0),
                arguments(targetZScore, true, 1.0, 1.5, Double.NaN), // NaN case
                arguments(targetZScore, false, 100000.0, 1.5, 2.0),
                arguments(targetZScore, true, null, 1.5, 2.0)
        );
    }
}
