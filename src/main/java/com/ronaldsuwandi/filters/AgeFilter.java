package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

import java.time.Instant;

// only filter quote that is not older than durationInMillis
public class AgeFilter implements QuoteFilter {
    private final long durationInMillis;

    public AgeFilter(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    @Override
    public FilterResult filter(Quote bid, Quote ask) {
        var bidFail = false;
        var askFail = false;
        var now = Instant.now().toEpochMilli();
        if (bid != null && (now - bid.getTimestamp() >= durationInMillis)) {
            bidFail = true;
        }
        if (ask != null && (now - ask.getTimestamp() >= durationInMillis)) {
            askFail = true;
        }
        return FilterResultHelper.getFilterResult(bidFail, askFail);
    }

}
