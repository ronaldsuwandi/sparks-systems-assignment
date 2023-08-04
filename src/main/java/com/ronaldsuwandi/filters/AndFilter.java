package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

import java.util.ArrayList;
import java.util.List;

public class AndFilter implements QuoteFilter {
    List<QuoteFilter> filters;

    private AndFilter(List<QuoteFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean filter(Quote quote) {
        for (QuoteFilter qf : filters) {
            var result = qf.filter(quote);
            if (!result) {
                // for AND filter, we can take shortcut to first condition that fails
                return false;
            }
        }
        return true;
    }

    public static class Builder {
        List<QuoteFilter> filters = new ArrayList<>();

        public Builder addFilter(QuoteFilter filter) {
            if (filter != null) filters.add(filter);
            return this;
        }

        public AndFilter build() {
            return new AndFilter(filters);
        }
    }
}
