package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

// Filters quote by symbol
public class SourceFilter implements QuoteFilter {
    private final String source;

    public SourceFilter(String source) {
        this.source = source;
    }

    @Override
    public boolean filter(Quote quote) {
        return quote != null && quote.getSource().equals(source);
    }
}
