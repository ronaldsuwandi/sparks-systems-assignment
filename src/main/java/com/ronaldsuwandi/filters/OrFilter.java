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
    public FilterResult filter(Quote bid, Quote ask) {
        boolean bidFails = false;
        boolean askFails = false;

        for (QuoteFilter qf : filters) {
            FilterResult result = qf.filter(bid, ask);
            if (result == FilterResult.Success) {
                // shortcut if there's any success
                return result;
            } else {
                switch(result) {
                    case BidFails: bidFails = true; break;
                    case AskFails: askFails = true; break;
                    case BidAskFails: {
                        bidFails = true;
                        askFails = true;
                        break;
                    }
                }
            }
        }

        return FilterResultHelper.getFilterResult(bidFails, askFails);
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
