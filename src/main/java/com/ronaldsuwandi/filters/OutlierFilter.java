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
    public FilterResult filter(Quote bid, Quote ask) {
        SymbolQuotes quotes = null;
        if (bid != null) {
            if (quotes == null) quotes = symbolQuotesMap.get(bid.getSymbol());
        }
        if (ask != null) {
            if (quotes == null) quotes = symbolQuotesMap.get(ask.getSymbol());
        }

        if (bid == null && ask == null) {
            return FilterResult.Success;
        }
        if (quotes == null) {
            throw new RuntimeException("symbol not found");
        }

        var bidFails = false;
        var askFails = false;
        if (bid != null) {
            double zscore = (bid.getQuote() - quotes.getQuoteAverage(bid.getType())) / quotes.getQuoteStdDev(bid.getType());
            if (!Double.isNaN(zscore)) bidFails = zscore > targetZScore;
        }

        if (ask != null) {
            double zscore = (ask.getQuote() - quotes.getQuoteAverage(ask.getType())) / quotes.getQuoteStdDev(ask.getType());
            if (!Double.isNaN(zscore)) askFails = zscore > targetZScore;
        }

        return FilterResultHelper.getFilterResult(bidFails, askFails);
    }
}
