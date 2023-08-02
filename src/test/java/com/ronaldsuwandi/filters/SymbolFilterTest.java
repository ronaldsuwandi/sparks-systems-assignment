package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SymbolFilterTest {
    @ParameterizedTest
    @MethodSource("filterArguments")
    public void testFilter(String symbol, FilterResult expected, Quote bid, Quote ask) {
        SymbolFilter filter = new SymbolFilter(symbol);
        assertEquals(expected, filter.filter(bid, ask));
    }

    static Stream<Arguments> filterArguments() {
        String symbol = "EURUSD";
        return Stream.of(
                arguments(
                        symbol,
                        FilterResult.Success,
                        new Quote(symbol, "reuters", 1.5, QuoteType.BID, 0),
                        new Quote(symbol, "reuters", 1.5, QuoteType.ASK, 0)
                ),
                arguments(
                        symbol,
                        FilterResult.AskFails,
                        new Quote(symbol, "reuters", 1.5, QuoteType.BID, 0),
                        new Quote("AUDUSD", "reuters", 1.5, QuoteType.ASK, 0)
                ),
                arguments(
                        symbol,
                        FilterResult.BidFails,
                        new Quote("SGDUSD", "reuters", 1.5, QuoteType.BID, 0),
                        new Quote(symbol, "reuters", 1.5, QuoteType.ASK, 0)
                ),
                arguments(
                        symbol,
                        FilterResult.BidAskFails,
                        new Quote("SGDUSD", "reuters", 1.5, QuoteType.BID, 0),
                        new Quote("AUDGBP", "reuters", 1.5, QuoteType.ASK, 0)
                ),
                arguments(
                        symbol,
                        FilterResult.Success,
                        new Quote(symbol, "reuters", 1.5, QuoteType.BID, 0),
                        null
                ),
                arguments(
                        symbol,
                        FilterResult.Success,
                        null,
                        new Quote(symbol, "reuters", 1.5, QuoteType.BID, 0)
                ),
                arguments(
                        symbol,
                        FilterResult.Success,
                        null,
                        null
                ),
                arguments(
                        symbol,
                        FilterResult.BidFails,
                        new Quote("SGDUSD", "reuters", 1.5, QuoteType.BID, 0),
                        null
                ),
                arguments(
                        symbol,
                        FilterResult.AskFails,
                        null,
                        new Quote("SGDUSD", "reuters", 1.5, QuoteType.BID, 0)
                )
        );
    }
}
