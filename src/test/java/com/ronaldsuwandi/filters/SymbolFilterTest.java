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
    public void testFilter(String symbol, boolean expected, String bidSymbol) {
        var filter = new SymbolFilter(symbol);
        var bid = bidSymbol != null ? new Quote(bidSymbol, "reuters", 1.5, QuoteType.BID, 0) : null;
        assertEquals(expected, filter.filter(bid));
    }

    static Stream<Arguments> filterArguments() {
        var symbol = "EURUSD";
        return Stream.of(
                arguments(symbol, true, symbol),
                arguments(symbol, false, "AUDUSD"),
                arguments(symbol, true, null)
        );
    }
}
