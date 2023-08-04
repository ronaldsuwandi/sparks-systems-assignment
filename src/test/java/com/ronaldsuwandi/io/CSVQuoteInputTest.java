package com.ronaldsuwandi.io;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CSVQuoteInputTest {

    @Test
    public void testRead() throws Exception {
        var input = "source,symbol,timestamp,bidPrice,askPrice\n" +
                "reuters,EURUSD,20230803000000099,1.305,1.44\n" +
                "bloomberg,AUDSGD,20230803000000099,0.887,0.91\n" +
                "bloomberg,EURUSD,20230803000000099,1.313,1.38\n".trim();
        var csvInput = new CSVQuoteInput(new StringReader(input));
        var timestamp = LocalDateTime.of(2023, Month.AUGUST, 3, 0, 0, 0, 99000000).toInstant(ZoneOffset.UTC).toEpochMilli();
        var actual = csvInput.read();

        assertEquals(2, actual.size());
        var expectedBids = List.of(
                new Quote("EURUSD", "reuters", 1.305, QuoteType.BID, timestamp),
                new Quote("EURUSD", "bloomberg", 1.313, QuoteType.BID, timestamp),
                new Quote("AUDSGD", "bloomberg", 0.887, QuoteType.BID, timestamp)
        );
        var expectedAsks = List.of(
                new Quote("EURUSD", "bloomberg", 1.38, QuoteType.ASK, timestamp),
                new Quote("EURUSD", "reuters", 1.44, QuoteType.ASK, timestamp),
                new Quote("AUDSGD", "bloomberg", 0.91, QuoteType.ASK, timestamp)
        );

        var actualSymbolQuotes = actual.get("EURUSD");
        var actualBidIterator = actualSymbolQuotes.iterator(QuoteType.BID);
        var actualAskIterator = actualSymbolQuotes.iterator(QuoteType.ASK);
        assertEquals(expectedBids.get(0), actualBidIterator.next());
        assertEquals(expectedBids.get(1), actualBidIterator.next());
        assertFalse(actualBidIterator.hasNext());
        assertEquals(expectedAsks.get(0), actualAskIterator.next());
        assertEquals(expectedAsks.get(1), actualAskIterator.next());
        assertFalse(actualAskIterator.hasNext());

        actualSymbolQuotes = actual.get("AUDSGD");
        actualBidIterator = actualSymbolQuotes.iterator(QuoteType.BID);
        actualAskIterator = actualSymbolQuotes.iterator(QuoteType.ASK);
        assertEquals(expectedBids.get(2), actualBidIterator.next());
        assertFalse(actualBidIterator.hasNext());
        assertEquals(expectedAsks.get(2), actualAskIterator.next());
        assertFalse(actualAskIterator.hasNext());
    }
}
