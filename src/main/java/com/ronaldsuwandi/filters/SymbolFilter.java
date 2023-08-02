package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

// Filters quote by symbol
public class SymbolFilter implements QuoteFilter {
    private final String symbol;

    public SymbolFilter(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public FilterResult filter(Quote bid, Quote ask) {
        boolean bidFail = false;
        boolean askFail = false;
        if (bid != null && !bid.getSymbol().equals(symbol)) {
            bidFail = true;
        }
        if (ask != null && !ask.getSymbol().equals(symbol)) {
            askFail = true;
        }
        return FilterResultHelper.getFilterResult(bidFail, askFail);
    }
}
