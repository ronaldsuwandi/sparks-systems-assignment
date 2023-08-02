package com.ronaldsuwandi;

import java.util.*;

public class SymbolQuotes {
    private final Map<String, NavigableSet<Quote>> bidsBySymbol;
    private final Map<String, NavigableSet<Quote>> asksBySymbol;


    public SymbolQuotes() {
        bidsBySymbol = new HashMap<>();
        asksBySymbol = new HashMap<>();
    }

    public void addQuote(Quote quote) {
        if (quote == null) {
            return; // FIXME
        }

        final NavigableSet<Quote> set;
        if (quote.getType() == QuoteType.BID) {
            set = bidsBySymbol.putIfAbsent(quote.getSymbol(), new TreeSet<>());
        } else {
            set = asksBySymbol.putIfAbsent(quote.getSymbol(), new TreeSet<>());
        }
        set.add(quote);
    }

    public Set<String> getSymbols() {
        Set<String> symbols = bidsBySymbol.keySet();
        symbols.addAll(asksBySymbol.keySet());
        return symbols;
    }

    public Iterator<Quote> iterator(String symbol, QuoteType type) {
        switch(type) {
            case ASK:
                return asksBySymbol.get(symbol).iterator();
            case BID:
                return bidsBySymbol.get(symbol).iterator();
        }
        return null;
    }
}
