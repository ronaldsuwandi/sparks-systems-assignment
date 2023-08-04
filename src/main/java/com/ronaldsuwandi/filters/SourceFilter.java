package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

// Filters quote by symbol
public class SourceFilter implements QuoteFilter {
    private final String source;

    public SourceFilter(String source) {
        this.source = source;
    }

    @Override
    public FilterResult filter(Quote bid, Quote ask) {
        var bidFail = false;
        var askFail = false;
        if (bid != null && !bid.getSource().equals(source)) {
            bidFail = true;
        }
        if (ask != null && !ask.getSource().equals(source)) {
            askFail = true;
        }
        return FilterResultHelper.getFilterResult(bidFail, askFail);
    }
}
