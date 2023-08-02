package com.ronaldsuwandi;

import com.ronaldsuwandi.filters.FilterResult;
import com.ronaldsuwandi.filters.QuoteFilter;

import java.util.Set;

public class QuotesProcessor {
    private final SymbolQuotes symbolQuotes;

    private final String output; // TODO

    public QuotesProcessor(SymbolQuotes symbolQuotes, String output) {
        this.symbolQuotes = symbolQuotes;
        this.output = output;
    }

    public void addFilter() {
        // TODO
    }

    private QuoteFilter filter;

    public void process() {
        Set<String> symbols = symbolQuotes.getSymbols();
        for (String symbol : symbols) {
            var bidIterator = symbolQuotes.iterator(symbol, QuoteType.BID);
            var askIterator = symbolQuotes.iterator(symbol, QuoteType.ASK);

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

                if (result != FilterResult.Success) {
                    switch(result) {
                        case AskFails: ask = null; break;
                        case BidFails: bid = null; break;
                        case BidAskFails: {
                            bid = null;
                            ask = null;
                            break;
                        }
                    }
                    continue;
                }

                // success for both
                bid = null;
                ask = null;
                // do output
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
