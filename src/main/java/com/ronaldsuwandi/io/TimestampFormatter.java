package com.ronaldsuwandi.io;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimestampFormatter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static String format(long timestampEpochMilli) {
        return Instant.ofEpochMilli(timestampEpochMilli).atZone(ZoneOffset.UTC).format(formatter);
    }

    public static long toEpochMillis(String datetime) {
        return LocalDateTime.parse(datetime, formatter).toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
