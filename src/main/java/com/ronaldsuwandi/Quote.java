package com.ronaldsuwandi;

import java.util.Objects;

public class Quote implements Comparable<Quote>{
    private final String symbol;
    private final String source;
    private final double quote;
    private final QuoteType type;
    private final long timestamp;

    public Quote(String symbol, String source, double quote, QuoteType type, long timestamp) {
        this.symbol = symbol;
        this.source = source;
        this.quote = quote;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getQuote() {
        return quote;
    }

    public String getSource() {
        return source;
    }

    public QuoteType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Quote o) {
        return Double.compare(this.quote, o.quote);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote1 = (Quote) o;
        return Double.compare(quote1.quote, quote) == 0 && timestamp == quote1.timestamp && Objects.equals(symbol, quote1.symbol) && Objects.equals(source, quote1.source) && type == quote1.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, source, quote, type, timestamp);
    }
}
