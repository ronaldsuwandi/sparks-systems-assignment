package com.ronaldsuwandi;

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
}
