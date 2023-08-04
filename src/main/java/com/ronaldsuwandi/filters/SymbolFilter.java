package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

// Filters quote by symbol
public class SymbolFilter implements QuoteFilter {
    private final String symbol;

    public SymbolFilter(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean filter(Quote quote) {
        return quote == null || quote.getSymbol().equals(symbol);
    }
}
