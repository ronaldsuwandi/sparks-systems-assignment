package com.ronaldsuwandi.filters;


import com.ronaldsuwandi.Quote;
import com.ronaldsuwandi.SymbolQuotes;

import java.util.Map;

// basic outlier filter that uses Z-Score algorithm - https://en.wikipedia.org/wiki/Standard_score
public class OutlierFilter implements QuoteFilter {
    private final Map<String, SymbolQuotes> symbolQuotesMap;
    private final double targetZScore;

    public OutlierFilter(Map<String, SymbolQuotes> symbolQuotesMap, double targetZScore) {
        this.symbolQuotesMap = symbolQuotesMap;
        this.targetZScore = targetZScore;
    }

    @Override
    public boolean filter(Quote quote) {
        if (quote == null) {
            return false;
        }

        var quotes = symbolQuotesMap.get(quote.getSymbol());
        if (quotes == null) {
            throw new RuntimeException("symbol not found");
        }

        double zscore = (quote.getQuote() - quotes.getQuoteAverage(quote.getType())) / quotes.getQuoteStdDev(quote.getType());
        if (Double.isNaN(zscore)) {
            return true;
        }
        return Math.abs(zscore) <= targetZScore;
    }
}
