package com.ronaldsuwandi.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TimestampFormatterTest {
    @ParameterizedTest
    @MethodSource("formatterArguments")
    public void testFormatter(String expected, long timestamp) {
        assertEquals(expected, TimestampFormatter.format(timestamp));
    }

    static Stream<Arguments> formatterArguments() {
        return Stream.of(
                arguments("20230803144941050", 1691074181050L),
                arguments("20221204073010123", 1670139010123L)
        );
    }

    @ParameterizedTest
    @MethodSource("toEpochMsArguments")
    public void testToEpochMillis(long expected, String timestamp) {
        assertEquals(expected, TimestampFormatter.toEpochMillis(timestamp));
    }

    static Stream<Arguments> toEpochMsArguments() {
        return Stream.of(
                arguments(1691074181050L, "20230803144941050"),
                arguments(1670139010123L, "20221204073010123")
        );
    }
}
