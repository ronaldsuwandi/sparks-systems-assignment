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
    public boolean filter(Quote quote) {
        var now = Instant.now().toEpochMilli();
        return quote != null && (now - quote.getTimestamp() < durationInMillis);
    }

}
