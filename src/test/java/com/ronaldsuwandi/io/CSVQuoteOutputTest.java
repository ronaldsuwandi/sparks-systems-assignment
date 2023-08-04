package com.ronaldsuwandi.io;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

public class CSVQuoteOutputTest {

    @Test
    public void testWrite() throws Exception {
        var writer = new StringWriter();
        var csvQuoteOutput = new CSVQuoteOutput(writer);
        var timestamp = LocalDateTime.of(2023, Month.AUGUST, 3, 0, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        var symbols = List.of("USDSGD", "SGDAUD", "USDSGD");
        var bids = List.of(
                new Quote("USDSGD", "reuters", 1.3, QuoteType.BID, timestamp),
                new Quote("SGDAUD", "bloomberg", 1.11, QuoteType.BID, timestamp),
                new Quote("USDSGD", "reuters", 1.31, QuoteType.BID, timestamp)
        );
        var asks = List.of(
                new Quote("USDSGD", "bloomberg", 1.461, QuoteType.ASK, timestamp + 10),
                new Quote("SGDAUD", "bloomberg", 1.115, QuoteType.ASK, timestamp + 10),
                new Quote("USDSGD", "reuters", 1.45, QuoteType.ASK, timestamp + 10)
        );

        for (var i = 0; i < symbols.size(); i++) {
            csvQuoteOutput.write(symbols.get(i), bids.get(i), asks.get(i));
        }
        var expected = "USDSGD,reuters,20230803000000000,1.3,bloomberg,20230803000000010,1.461\r\n" +
                "SGDAUD,bloomberg,20230803000000000,1.11,bloomberg,20230803000000010,1.115\r\n" +
                "USDSGD,reuters,20230803000000000,1.31,reuters,20230803000000010,1.45";
        Assertions.assertEquals(expected, writer.toString().trim());
    }
}
