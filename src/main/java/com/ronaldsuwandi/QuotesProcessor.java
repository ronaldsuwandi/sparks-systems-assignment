package com.ronaldsuwandi;

import com.ronaldsuwandi.filters.FilterResult;
import com.ronaldsuwandi.filters.QuoteFilter;
import com.ronaldsuwandi.io.QuoteOutput;

import java.io.IOException;
import java.util.Map;

public class QuotesProcessor {
    private final QuoteOutput output;

    public QuotesProcessor(QuoteOutput output) {
        this.output = output;
    }

    private QuoteFilter filter;

    public void setFilter(QuoteFilter filter) {
        this.filter = filter;
    }

    public void process(Map<String, SymbolQuotes> symbolQuotesMap) {

        for (var entry : symbolQuotesMap.entrySet()) {
            var symbol = entry.getKey();
            var symbolQuotes = entry.getValue();
            var bidIterator = symbolQuotes.iterator(QuoteType.BID);
            var askIterator = symbolQuotes.iterator(QuoteType.ASK);

            Quote bid = null;
            Quote ask = null;

            while (bidIterator.hasNext() || askIterator.hasNext()) {
                if (bid == null && bidIterator.hasNext()) {
                    bid = bidIterator.next();
                }
                if (ask == null && askIterator.hasNext()) {
                    ask = askIterator.next();
                }

                var result = filter == null ? FilterResult.Success : filter.filter(bid, ask);

                switch (result) {
                    case AskFails:
                        ask = null;
                        break;
                    case BidFails:
                        bid = null;
                        break;

                    case BidAskFails:
                        bid = null;
                        ask = null;
                        break;

                    case Success: {
                        try {
                            output.write(symbol, bid, ask);
                        } catch (IOException e) {
                            throw new RuntimeException("error writing output", e);
                        }
                        bid = null;
                        ask = null;
                        break;
                    }
                }

            }
        }


    }
}
