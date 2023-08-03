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
    public void testFilter(double targetZScore, FilterResult expected,
                           Double bidQuote, Double askQuote,
                           double bidAverage, double askAverage,
                           double bidStdDev, double askStdDev) {
        SymbolQuotes quotes = mock(SymbolQuotes.class);
        when(quotes.getQuoteAverage(QuoteType.BID)).thenReturn(bidAverage);
        when(quotes.getQuoteAverage(QuoteType.ASK)).thenReturn(askAverage);
        when(quotes.getQuoteStdDev(QuoteType.BID)).thenReturn(bidStdDev);
        when(quotes.getQuoteStdDev(QuoteType.ASK)).thenReturn(askStdDev);

        OutlierFilter filter = new OutlierFilter(Map.of("EURUSD", quotes), targetZScore);
        Quote bid = bidQuote != null ? new Quote("EURUSD", "reuters", bidQuote, QuoteType.BID, 0) : null;
        Quote ask = askQuote != null ? new Quote("EURUSD", "reuters", askQuote, QuoteType.ASK, 0) : null;
        assertEquals(expected, filter.filter(bid, ask));
    }

    static Stream<Arguments> filterArguments() {
        double targetZScore = 2.0;
        return Stream.of(
                arguments(targetZScore, FilterResult.Success, 1.0, 1.0, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.Success, 1.0, 1.0, 1.5, 1.2, Double.NaN, Double.NaN), // NaN case
                arguments(targetZScore, FilterResult.BidFails, 100000.0, 1.0, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.AskFails, 1.0, 100000.0, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.BidAskFails, 100000.0, 100000.0, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.Success, 1.0, null, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.Success, null, 1.0, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.Success, null, null, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.BidFails, 100000.0, null, 1.5, 1.2, 2.0, 0.8),
                arguments(targetZScore, FilterResult.AskFails, null, 100000.0, 1.5, 1.2, 2.0, 0.8)
                );
    }
}
