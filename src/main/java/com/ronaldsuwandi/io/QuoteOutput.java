package com.ronaldsuwandi.io;

import com.ronaldsuwandi.Quote;

import java.io.Flushable;
import java.io.IOException;

public interface QuoteOutput extends Flushable, AutoCloseable {
    void write(String symbol, Quote bid, Quote ask) throws IOException;
}
