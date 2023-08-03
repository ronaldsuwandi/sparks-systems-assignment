package com.ronaldsuwandi.io;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimestampFormatter {
    private static final DateTimeFormatter QuoteFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String format(long timestampEpochMilli) {
        return Instant.ofEpochMilli(timestampEpochMilli).atZone(ZoneOffset.UTC).format(QuoteFormatter);
    }
}
