package com.ronaldsuwandi.filters;

import com.ronaldsuwandi.Quote;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

public class AndOrFilterTest {
    private static final QuoteFilter success = (quote) -> true;
    private static final QuoteFilter fail = (quote) -> false;

    private static final Quote bidMock = mock(Quote.class);
    private static final Quote askMock = mock(Quote.class);

    @ParameterizedTest
    @MethodSource("filterAndArguments")
    public void testAndFilter(boolean expected, List<QuoteFilter> filters) {
        var filterBuilder = new AndFilter.Builder();
        AndFilter andFilter;
        for (var filter : filters) {
            filterBuilder.addFilter(filter);
        }
        andFilter = filterBuilder.build();
        assertEquals(expected, andFilter.filter(bidMock));
    }

    static Stream<Arguments> filterAndArguments() {
        return Stream.of(
                arguments(true, List.of(success, success, success)),
                arguments(false, List.of(fail, success, success)),
                arguments(false, List.of(fail, fail, success)),
                arguments(false, List.of(fail, fail, fail))
        );
    }

    @ParameterizedTest
    @MethodSource("filterOrArguments")
    public void testOrFilter(boolean expected, List<QuoteFilter> filters) {
        var filterBuilder = new OrFilter.Builder();
        OrFilter orFilter;
        for (var filter : filters) {
            filterBuilder.addFilter(filter);
        }
        orFilter = filterBuilder.build();
        assertEquals(expected, orFilter.filter(bidMock));
    }

    static Stream<Arguments> filterOrArguments() {
        return Stream.of(
                arguments(true, List.of(success, success, success)),
                arguments(true, List.of(fail, success, success)),
                arguments(true, List.of(fail, fail, success)),
                arguments(false, List.of(fail, fail, fail))
        );
    }


    @ParameterizedTest
    @MethodSource("filterCombinedAndOrArguments")
    public void testCombinedAndOrFilter(boolean expected, QuoteFilter firstAndFilter, List<QuoteFilter> orFilters) {
        var orFilterBuilder = new OrFilter.Builder();
        OrFilter orFilter;
        for (var filter : orFilters) {
            orFilterBuilder.addFilter(filter);
        }
        orFilter = orFilterBuilder.build();

        var andFilterBuilder = new AndFilter.Builder();
        AndFilter andFilter;
        andFilter = andFilterBuilder.addFilter(firstAndFilter)
                .addFilter(orFilter)
                .build();

        assertEquals(expected, andFilter.filter(bidMock));
    }

    static Stream<Arguments> filterCombinedAndOrArguments() {
        return Stream.of(
                arguments(true, success, List.of(success, success)),
                arguments(true, success, List.of(fail, success)),
                arguments(true, success, List.of(success, fail)),
                arguments(false, success, List.of(fail, fail)),
                arguments(false, fail, List.of(success, success)),
                arguments(false, fail, List.of(success, fail)),
                arguments(false, fail, List.of(fail, fail))
        );
    }
}
