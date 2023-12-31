package com.ronaldsuwandi;


import com.ronaldsuwandi.filters.*;
import com.ronaldsuwandi.io.CSVQuoteInput;
import com.ronaldsuwandi.io.CSVQuoteOutput;
import com.ronaldsuwandi.io.QuoteInput;
import com.ronaldsuwandi.io.QuoteOutput;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

// e2e test
public class MainTest {
    @Test
    public void testE2ESmallData() throws Exception {

        var inputStream = Main.class.getResourceAsStream("/quotes-small.csv");
        var expectedInputStream = Main.class.getResourceAsStream("/output-quotes-small.csv");

        if (inputStream == null || expectedInputStream == null) {
            throw new RuntimeException("resource not found");
        }
        var writer = new StringWriter();

        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer);
                BufferedReader expectedInput = new BufferedReader(new InputStreamReader(expectedInputStream));
        ) {
            var map = input.read();

            QuotesProcessor processor = new QuotesProcessor(output);
            var orFilter = new OrFilter.Builder()
                    .addFilter(new SourceFilter("bloomberg"))
                    .addFilter(new SourceFilter("saxo"))
                    .build();
            var filter = new AndFilter.Builder()
                    .addFilter(new SymbolFilter("EURUSD"))
                    .addFilter(orFilter)
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();

            assertTrue(IOUtils.contentEqualsIgnoreEOL(expectedInput, new StringReader(writer.toString())));
        }
    }

    @Test
    public void testE2ESmallDataOutlier() throws Exception {

        var inputStream = Main.class.getResourceAsStream("/quotes-small-outlier.csv");
        var expectedInputStream = Main.class.getResourceAsStream("/output-quotes-small-outlier.csv");
        if (inputStream == null || expectedInputStream == null) {
            return;
        }

        var writer = new StringWriter();
        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer);
                BufferedReader expectedInput = new BufferedReader(new InputStreamReader(expectedInputStream));
        ) {
            var map = input.read();
            QuotesProcessor processor = new QuotesProcessor(output);
            var filter = new AndFilter.Builder()
                    .addFilter(new OutlierFilter(map, 3d))
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();

            assertTrue(IOUtils.contentEqualsIgnoreEOL(expectedInput, new StringReader(writer.toString())));
        }
    }

    @Test
    public void testE2ELargerData() throws Exception {
        var inputStream = Main.class.getResourceAsStream("/quotes.csv");
        var expectedInputStream = Main.class.getResourceAsStream("/output-quotes.csv");

        if (inputStream == null || expectedInputStream == null) {
            throw new RuntimeException("resource not found");
        }
        var writer = new StringWriter();

        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer);
                BufferedReader expectedInput = new BufferedReader(new InputStreamReader(expectedInputStream));
        ) {
            var map = input.read();
            QuotesProcessor processor = new QuotesProcessor(output);
            var orFilter = new OrFilter.Builder()
                    .addFilter(new SourceFilter("bloomberg"))
                    .addFilter(new SourceFilter("saxo"))
                    .build();
            var filter = new AndFilter.Builder()
                    .addFilter(new SymbolFilter("USDSGD"))
                    .addFilter(orFilter)
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();

            assertTrue(IOUtils.contentEqualsIgnoreEOL(expectedInput, new StringReader(writer.toString())));
        }
    }

    @Test
    public void testE2E() throws Exception {
        // this test is just to print out the output into csv file, more for showcase
        var inputStream = Main.class.getResourceAsStream("/quotes.csv");
        if (inputStream == null) {
            throw new RuntimeException("resource not found");
        }
        var writer = new BufferedWriter(new FileWriter("output.csv"));
        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer);
        ) {
            var map = input.read();
            QuotesProcessor processor = new QuotesProcessor(output);
            var filter = new AndFilter.Builder()
                    .addFilter(new SymbolFilter("USDSGD"))
                    .addFilter(new SourceFilter("reuters"))
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();
        }
    }

}
