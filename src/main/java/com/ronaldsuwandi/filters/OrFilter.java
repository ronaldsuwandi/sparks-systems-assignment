package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

import java.util.ArrayList;
import java.util.List;

public class OrFilter implements QuoteFilter {
    List<QuoteFilter> filters;

    private OrFilter(List<QuoteFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean filter(Quote quote) {
        for (QuoteFilter qf : filters) {
            var result = qf.filter(quote);
            if (result) {
                // shortcut if there's any success
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        List<QuoteFilter> filters = new ArrayList<>();

        public Builder addFilter(QuoteFilter filter) {
            if (filter != null) filters.add(filter);
            return this;
        }

        public OrFilter build() {
            return new OrFilter(filters);
        }
    }
}
