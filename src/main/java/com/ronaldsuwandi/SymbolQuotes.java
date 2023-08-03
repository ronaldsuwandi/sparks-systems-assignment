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

    private double bidStdDev;
    private double askStdDev;

    private double bidLastSum = 0; // used to maintain the latest sum to avoid iterating through the loop
    private double askLastSum = 0; // used to maintain the latest sum to avoid iterating through the loop
    private double bidLastSquareSum = 0; // used to maintain the squared sum to avoid iterating through the loop for variance/std deviation
    private double askLastSquareSum = 0; // used to maintain the squared sum to avoid iterating through the loop for variance/std deviation


    public SymbolQuotes(String symbol) {
        this.symbol = symbol;
    }

    // adds the quote into the list of quotes, it also update the last sum and last squared sum (used for z-score)
    public void addQuote(Quote quote) {
        if (quote == null) {
            return; // FIXME
        }

        double delta = 0;

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

    public Iterator<Quote> iterator(String symbol, QuoteType quoteType) {
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
        double sum = 0;
        double count = 0;
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

}
