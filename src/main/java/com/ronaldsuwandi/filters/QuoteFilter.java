package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;

public interface QuoteFilter {
    FilterResult filter(Quote bid, Quote ask);
}
