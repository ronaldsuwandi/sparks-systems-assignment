package com.ronaldsuwandi;

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

                var bidResult = filter == null || filter.filter(bid);
                var askResult = filter == null || filter.filter(ask);

                if (bidResult && askResult) {
                    try {
                        output.write(symbol, bid, ask);
                    } catch (IOException e) {
                        throw new RuntimeException("error writing output", e);
                    }
                    bid = null;
                    ask = null;
                } else {
                    if (!bidResult && !askResult) {
                        bid = null;
                        ask = null;
                    } else {
                        if (!bidResult) {
                            bid = null;
                        } else {
                            ask = null;
                        }
                        // FIXME check if this is the last row
                        if (!bidIterator.hasNext() && !askIterator.hasNext()) {
                            try {
                                output.write(symbol, bid, ask);
                            } catch (IOException e) {
                                throw new RuntimeException("error writing output", e);
                            }
                        }
                    }
                }


//                switch (result) {
//                    case AskFails:
//                        ask = null;
//                        // if last entry, just print out
//                        // FIXME check if ok
//                        if (!bidIterator.hasNext() && !askIterator.hasNext()) {
//                            try {
//                                output.write(symbol, bid, ask);
//                            } catch (IOException e) {
//                                throw new RuntimeException("error writing output", e);
//                            }
//                        }
//                        break;
//                    case BidFails:
//                        bid = null;
//                        // if last entry, just print out
//                        // FIXME check if ok
//                        if (!bidIterator.hasNext() && !askIterator.hasNext()) {
//                            try {
//                                output.write(symbol, bid, ask);
//                            } catch (IOException e) {
//                                throw new RuntimeException("error writing output", e);
//                            }
//                        }
//
//                        break;
//
//                    case BidAskFails:
//                        bid = null;
//                        ask = null;
//                        break;
//
//                    case Success: {
//                        try {
//                            output.write(symbol, bid, ask);
//                        } catch (IOException e) {
//                            throw new RuntimeException("error writing output", e);
//                        }
//                        bid = null;
//                        ask = null;
//                        break;
//                    }
//                }

            }
        }


    }
}
