package com.ronaldsuwandi;

import com.ronaldsuwandi.filters.FilterResult;
import com.ronaldsuwandi.filters.QuoteFilter;

import java.util.Map;

public class QuotesProcessor {
    private final String output; // TODO

    public QuotesProcessor(String output) {
        this.output = output;
    }

    private QuoteFilter filter;

    public void setFilter(QuoteFilter filter) {
        this.filter = filter;
    }

    public void process(Map<String, SymbolQuotes> symbolQuotesMap) {

        for (SymbolQuotes symbolQuotes : symbolQuotesMap.values()) {
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

                var result = filter.filter(bid, ask);

                switch (result) {
                    case AskFails:
                        ask = null;
                        break;
                    case BidFails:
                        bid = null;
                        break;

                    case BidAskFails:
                    case Success: {
                        bid = null;
                        ask = null;
                        break;
                    }
                }

                if (result == FilterResult.Success) {
                    // do output
                }
            }
        }


    }
}

        /*

        // for each symbol
        bidIterator = bids.iterator
        askIterator = asks.iterator

        bid = null
        ask = null

        while (bid.hasnext and ask.hasnext) {

            if (bid == null && bid.hasnext) {
                bid = bid.next
            }
            if (ask == null && ask.hasnext) {
                ask = ask.next
            }

            for (filter f : filters) {
                filterresult fr = f.filter(bid, ask)
                if (fr.success){
                    // success for both
                } else {
                    if fr.bidFails {
                        bid = null
                    }

                    if fr.askFails {
                        ask = null
                    }
                    continue
                }

            }

            // success for both
            bid = null
            ask = null
            output(bid, ask)

        }

        ----

        public filterresult filter(bid, ask)


        filterresult {
            success: bool
            bidFail: bid
            fail

        ---


         */
