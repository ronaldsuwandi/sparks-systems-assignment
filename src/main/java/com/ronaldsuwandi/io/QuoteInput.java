package com.ronaldsuwandi.io;

import com.ronaldsuwandi.SymbolQuotes;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface QuoteInput extends Closeable {
    Map<String, SymbolQuotes> read() throws IOException;
}
