package com.ronaldsuwandi.io;

import com.ronaldsuwandi.SymbolQuotes;

import java.io.IOException;
import java.util.Map;

public interface QuoteInput extends AutoCloseable {
    Map<String, SymbolQuotes> read() throws IOException;
}
