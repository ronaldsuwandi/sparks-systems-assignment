package com.ronaldsuwandi;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SymbolQuotesTest {
    @Test
    public void testSymbolQuotes() {
        var quotes = List.of(
                new Quote("USDSGD", "reuters", 1.3, QuoteType.BID, Instant.now().toEpochMilli()),
                new Quote("USDSGD", "reuters", 1.461, QuoteType.ASK, Instant.now().toEpochMilli()),
                new Quote("USDSGD", "reuters", 1.35, QuoteType.BID, Instant.now().toEpochMilli()),
                new Quote("USDSGD", "reuters", 1.291, QuoteType.BID, Instant.now().toEpochMilli()),
                new Quote("USDSGD", "reuters", 1.6, QuoteType.ASK, Instant.now().toEpochMilli()),
                new Quote("USDSGD", "reuters", 1.301, QuoteType.ASK, Instant.now().toEpochMilli())
        );

        var symbolQuotes = new SymbolQuotes("USDSGD");
        symbolQuotes.addQuote(quotes.get(0));
        symbolQuotes.addQuote(quotes.get(1));

        var bidIterator = symbolQuotes.iterator(QuoteType.BID);
        var askIterator = symbolQuotes.iterator(QuoteType.ASK);
        assertEquals(quotes.get(0), bidIterator.next());
        assertEquals(quotes.get(1), askIterator.next());
        assertEquals(1.3, symbolQuotes.getQuoteAverage(QuoteType.BID), 1E-6);
        assertEquals(1.461, symbolQuotes.getQuoteAverage(QuoteType.ASK), 1E-6);
        assertEquals(Double.NaN, symbolQuotes.getQuoteStdDev(QuoteType.BID), 1E-6);
        assertEquals(Double.NaN, symbolQuotes.getQuoteStdDev(QuoteType.ASK), 1E-6);

        // add remaining quotes
        symbolQuotes.addQuote(quotes.get(2));
        symbolQuotes.addQuote(quotes.get(3));
        symbolQuotes.addQuote(quotes.get(4));
        symbolQuotes.addQuote(quotes.get(5));

        bidIterator = symbolQuotes.iterator(QuoteType.BID);
        askIterator = symbolQuotes.iterator(QuoteType.ASK);
        assertEquals(quotes.get(3), bidIterator.next()); // best bid should be 1.291
        assertEquals(quotes.get(5), askIterator.next()); // best ask should be 1.301
        assertEquals(1.31366667, symbolQuotes.getQuoteAverage(QuoteType.BID), 1E-6);
        assertEquals(1.454, symbolQuotes.getQuoteAverage(QuoteType.ASK), 1E-6);
        assertEquals(0.03178574, symbolQuotes.getQuoteStdDev(QuoteType.BID), 1E-6);
        assertEquals(0.14962285, symbolQuotes.getQuoteStdDev(QuoteType.ASK), 1E-6);
    }
}
