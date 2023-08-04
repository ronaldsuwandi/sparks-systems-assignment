package com.ronaldsuwandi.io;

import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.QuoteType;
import com.ronaldsuwandi.SymbolQuotes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CSVQuoteInput implements QuoteInput {
    private final CSVParser csvParser;

    public CSVQuoteInput(Reader reader) throws IOException {
        this.csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    }

    private Quote fromRecord(String symbol, QuoteType quoteType, String source, String timestamp, Double quote) {
        if (symbol == null || source == null || timestamp == null || quote == null) {
            return null;
        }

        long timestampMs = TimestampFormatter.toEpochMillis(timestamp);

        return new Quote(symbol, source, quote, quoteType, timestampMs);
    }

    @Override
    public Map<String, SymbolQuotes> read() throws IOException {
        final Iterator<CSVRecord> csvRecordIterator = csvParser.iterator();
        final Map<String, SymbolQuotes> result = new HashMap<>();
        while (csvRecordIterator.hasNext()) {
            CSVRecord record = csvRecordIterator.next();
            long line = record.getRecordNumber();
            try {
                String source = record.get(0);
                String symbol = record.get(1);
                String timestamp = record.get(2);
                Double bidQuote = Double.parseDouble(record.get(3));
                Double askQuote = Double.parseDouble(record.get(4));

                // TODO helper for creaitng quote obj
                SymbolQuotes symbolQuotes = result.get(symbol);
                if (symbolQuotes == null) {
                    symbolQuotes = new SymbolQuotes(symbol);
                    result.put(symbol, symbolQuotes);
                }
                Quote bid = fromRecord(symbol, QuoteType.BID, source, timestamp, bidQuote);
                Quote ask = fromRecord(symbol, QuoteType.ASK, source, timestamp, askQuote);
                if (bid != null) symbolQuotes.addQuote(bid);
                if (ask != null) symbolQuotes.addQuote(ask);
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException | DateTimeParseException e) {
                throw new RuntimeException("Error parsing CSV record at line: " + line, e);
            }
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        this.csvParser.close();
    }
}
