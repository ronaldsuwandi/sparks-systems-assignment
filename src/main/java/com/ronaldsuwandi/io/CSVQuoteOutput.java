package com.ronaldsuwandi.io;

import com.ronaldsuwandi.Quote;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CSVQuoteOutput implements QuoteOutput {

    private final CSVPrinter csvPrinter;

    public CSVQuoteOutput(Writer writer) throws IOException {
        var csvFormat = CSVFormat.Builder.create()
                .setHeader("symbol", "bidSource", "bidTimestamp", "bidPrice", "askSource", "askTimestamp", "askPrice")
                .build();
        this.csvPrinter = new CSVPrinter(writer, csvFormat);
    }

    @Override
    public void flush() throws IOException {
        csvPrinter.flush();
    }

    @Override
    public void close() throws IOException {
        csvPrinter.close();
    }

    private List<Object> writeQuote(Quote quote) {
        List<Object> toWrite = new ArrayList<>();
        if (quote == null) {
            toWrite.add("");
            toWrite.add("");
            toWrite.add("");
        } else {
            toWrite.add(quote.getSource());
            toWrite.add(TimestampFormatter.format(quote.getTimestamp()));
            toWrite.add(quote.getQuote());
        }
        return toWrite;
    }
    @Override
    public void write(String symbol, Quote bid, Quote ask) throws IOException {
        List<Object> toWrite = new ArrayList<>();
        toWrite.add(symbol);
        toWrite.addAll(writeQuote(bid));
        toWrite.addAll(writeQuote(ask));
        csvPrinter.printRecord(toWrite);
    }
}
