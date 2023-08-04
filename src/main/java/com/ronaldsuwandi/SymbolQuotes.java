package com.ronaldsuwandi;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

public class SymbolQuotes {
    private final String symbol;
    private final NavigableSet<Quote> bids = new TreeSet<>();
    private final NavigableSet<Quote> asks = new TreeSet<>();

    private double bidAverage;
    private double askAverage;

    private double bidSumDifferenceFromMean;
    private double askSumDifferenceFromMean;

    public SymbolQuotes(String symbol) {
        this.symbol = symbol;
    }

    // adds the quote into the list of quotes, it also update the last sum and last squared sum (used for z-score)
    public void addQuote(Quote quote) {
        if (quote == null) {
            return;
        }
        if (!quote.getSymbol().equals(symbol)) {
            throw new RuntimeException("Symbol '" + quote.getSymbol() + "' does not match with SymbolQuotes ('" + symbol + "')");
        }

        double delta;
        if (quote.getType() == QuoteType.BID) {
            bids.add(quote);
            delta = quote.getQuote() - bidAverage;
            bidAverage += delta / (double) bids.size();
            bidSumDifferenceFromMean += delta * (quote.getQuote() - bidAverage);
        } else {
            asks.add(quote);
            delta = quote.getQuote() - askAverage;
            askAverage += delta / (double) asks.size();
            askSumDifferenceFromMean += delta * (quote.getQuote() - askAverage);
        }
    }

    public Iterator<Quote> iterator(QuoteType quoteType) {
        if (quoteType == QuoteType.BID) {
            return bids.iterator();
        } else {
            return asks.iterator();
        }
    }

    public double getQuoteAverage(QuoteType quoteType) {
        if (quoteType == QuoteType.BID) {
            return bidAverage;
        } else {
            return askAverage;
        }
    }

    public double getQuoteStdDev(QuoteType quoteType) {
        // reference for online variance: https://math.stackexchange.com/a/1769248
        var sum = 0d;
        var count = 0d;
        if (quoteType == QuoteType.BID) {
            sum = bidSumDifferenceFromMean;
            count = bids.size();
        } else {
            sum = askSumDifferenceFromMean;
            count = asks.size();
        }
        double variance = sum / (count - 1);
        return Math.sqrt(variance);
    }

    public String getSymbol() {
        return symbol;
    }
}
