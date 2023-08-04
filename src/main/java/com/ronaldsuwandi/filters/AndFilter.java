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
    public FilterResult filter(Quote bid, Quote ask) {
        for (QuoteFilter qf : filters) {
            FilterResult result = qf.filter(bid, ask);
            if (result != FilterResult.Success) {
                // for AND filter, we can take shortcut to first condition that fails
                return result;
            }
        }
        return FilterResult.Success;
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
