package com.ronaldsuwandi.io;

import com.ronaldsuwandi.SymbolQuotes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CSVQuoteInput implements QuoteInput {
    private final CSVParser csvParser;

    public CSVQuoteInput(Reader reader) throws IOException {
        this.csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    }

    @Override
    public Map<String, SymbolQuotes> read() throws IOException {
        final Iterator<CSVRecord> csvRecordIterator = csvParser.iterator();
        final Map<String, SymbolQuotes> result = new HashMap<>();
        while (csvRecordIterator.hasNext()) {
            CSVRecord record = csvRecordIterator.next();
            long line = record.getRecordNumber();
            try {
                String symbol = record.get(0);
                String bidSource = record.get(1);
                String bidTimestamp = record.get(2);
                Double bidQuote = Double.parseDouble(record.get(3));
                String askSource = record.get(4);
                String askTimestamp = record.get(5);
                Double askQuote = Double.parseDouble(record.get(6));

                // TODO helper for creaitng quote obj

            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
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
