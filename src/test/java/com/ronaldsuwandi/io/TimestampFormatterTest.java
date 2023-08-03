package com.ronaldsuwandi.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TimestampFormatterTest {
    @ParameterizedTest
    @MethodSource("filterArguments")
    public void testFormatter(String expected, long timestamp) {
        assertEquals(expected, TimestampFormatter.format(timestamp));
    }

    static Stream<Arguments> filterArguments() {
        return Stream.of(
                arguments("20230803144941050", 1691074181050L),
                arguments("20221204073010123", 1670139010123L)
        );
    }
}
