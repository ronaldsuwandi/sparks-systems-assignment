package com.ronaldsuwandi;


import com.ronaldsuwandi.filters.AndFilter;
import com.ronaldsuwandi.filters.OutlierFilter;
import com.ronaldsuwandi.filters.SourceFilter;
import com.ronaldsuwandi.filters.SymbolFilter;
import com.ronaldsuwandi.io.CSVQuoteInput;
import com.ronaldsuwandi.io.CSVQuoteOutput;
import com.ronaldsuwandi.io.QuoteInput;
import com.ronaldsuwandi.io.QuoteOutput;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

// e2e test
public class MainTest {
    @Test
    public void testE2ESmallData() throws Exception {

        var inputStream = Main.class.getResourceAsStream("/" + "quotes-small.csv");
        if (inputStream == null) {
            return;
        }
        var writer = new StringWriter();

        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer)
        ) {
            var map = input.read();

            QuotesProcessor processor = new QuotesProcessor(output);
            var filter = new AndFilter.Builder()
                    .addFilter(new SourceFilter("bloomberg"))
                    .addFilter(new SymbolFilter("EURUSD"))
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();

            System.out.println(writer);
        }
    }

    @Test
    public void testE2ESmallDataOutlier() throws Exception {

        var inputStream = Main.class.getResourceAsStream("/" + "quotes-small-outlier.csv");
        if (inputStream == null) {
            return;
        }
        var writer = new StringWriter();

        try (
                QuoteInput input = new CSVQuoteInput(new BufferedReader(new InputStreamReader(inputStream)));
                QuoteOutput output = new CSVQuoteOutput(writer)
        ) {
            var map = input.read();

            QuotesProcessor processor = new QuotesProcessor(output);
            var filter = new AndFilter.Builder()
                    .addFilter(new OutlierFilter(map, 3d))
                    .build();
            processor.setFilter(filter);
            processor.process(map);
            output.flush();

            System.out.println(writer);
        }
    }

    public void testE2ELargerData() throws Exception {

    }
}
