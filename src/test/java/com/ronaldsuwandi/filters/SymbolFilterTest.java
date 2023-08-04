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
    public void testFilter(String symbol, FilterResult expected, String bidSymbol, String askSymbol) {
        var filter = new SymbolFilter(symbol);
        var bid = bidSymbol != null ? new Quote(bidSymbol, "reuters", 1.5, QuoteType.BID, 0) : null;
        var ask = askSymbol != null ? new Quote(askSymbol, "reuters", 1.5, QuoteType.ASK, 0) : null;
        assertEquals(expected, filter.filter(bid, ask));
    }

    static Stream<Arguments> filterArguments() {
        var symbol = "EURUSD";
        return Stream.of(
                arguments(symbol, FilterResult.Success, symbol, symbol),
                arguments(symbol, FilterResult.AskFails, symbol, "AUDUSD"),
                arguments(symbol, FilterResult.BidFails, "SGDUSD", symbol),
                arguments(symbol, FilterResult.BidAskFails, "SGDUSD", "AUDGBP"),
                arguments(symbol, FilterResult.Success, symbol, null),
                arguments(symbol, FilterResult.Success, null, symbol),
                arguments(symbol, FilterResult.Success, null, null),
                arguments(symbol, FilterResult.BidFails, "SGDUSD", null),
                arguments(symbol, FilterResult.AskFails, null, "SGDUSD")
        );
    }
}
