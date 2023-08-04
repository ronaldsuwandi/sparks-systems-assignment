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
    private static final QuoteFilter success = (bid, ask) -> FilterResult.Success;
    private static final QuoteFilter bidFails = (bid, ask) -> FilterResult.BidFails;
    private static final QuoteFilter askFails = (bid, ask) -> FilterResult.AskFails;
    private static final QuoteFilter bidAskFails = (bid, ask) -> FilterResult.BidAskFails;

    private static final Quote bidMock = mock(Quote.class);
    private static final Quote askMock = mock(Quote.class);

    @ParameterizedTest
    @MethodSource("filterAndArguments")
    public void testAndFilter(FilterResult expected, List<QuoteFilter> filters) {
        var filterBuilder = new AndFilter.Builder();
        AndFilter andFilter;
        for (var filter : filters) {
            filterBuilder.addFilter(filter);
        }
        andFilter = filterBuilder.build();
        assertEquals(expected, andFilter.filter(bidMock, askMock));
    }

    static Stream<Arguments> filterAndArguments() {
        return Stream.of(
                arguments(FilterResult.Success, List.of(success, success, success)),
                arguments(FilterResult.BidFails, List.of(success, success, bidFails)),
                arguments(FilterResult.AskFails, List.of(success, askFails, bidFails)),
                arguments(FilterResult.AskFails, List.of(success, askFails, success)),
                arguments(FilterResult.BidAskFails, List.of(bidAskFails, success, success))
        );
    }

    @ParameterizedTest
    @MethodSource("filterOrArguments")
    public void testOrFilter(FilterResult expected, List<QuoteFilter> filters) {
        var filterBuilder = new OrFilter.Builder();
        OrFilter orFilter;
        for (var filter : filters) {
            filterBuilder.addFilter(filter);
        }
        orFilter = filterBuilder.build();
        assertEquals(expected, orFilter.filter(bidMock, askMock));
    }

    static Stream<Arguments> filterOrArguments() {
        return Stream.of(
                arguments(FilterResult.Success, List.of(success, success, success)),
                arguments(FilterResult.Success, List.of(bidFails, success, success)),
                arguments(FilterResult.Success, List.of(bidFails, askFails, success)),
                arguments(FilterResult.BidFails, List.of(bidFails, bidFails, bidFails)),
                arguments(FilterResult.AskFails, List.of(askFails, askFails, askFails)),
                arguments(FilterResult.BidAskFails, List.of(askFails, askFails, bidFails))
        );
    }


    @ParameterizedTest
    @MethodSource("filterCombinedAndOrArguments")
    public void testCombinedAndOrFilter(FilterResult expected, QuoteFilter firstAndFilter, List<QuoteFilter> orFilters) {
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

        assertEquals(expected, andFilter.filter(bidMock, askMock));
    }

    static Stream<Arguments> filterCombinedAndOrArguments() {
        return Stream.of(
                arguments(FilterResult.Success, success, List.of(success, success)),
                arguments(FilterResult.Success, success, List.of(bidFails, success)),
                arguments(FilterResult.Success, success, List.of(success, askFails)),
                arguments(FilterResult.Success, success, List.of(bidFails, askFails, success)),
                arguments(FilterResult.BidFails, success, List.of(bidFails, bidFails)),
                arguments(FilterResult.BidFails, bidFails, List.of(success, success)),
                arguments(FilterResult.AskFails, success, List.of(askFails, askFails)),
                arguments(FilterResult.AskFails, askFails, List.of(success, success)),
                arguments(FilterResult.AskFails, askFails, List.of(bidFails, bidFails)),
                arguments(FilterResult.BidAskFails, bidAskFails, List.of(bidFails, bidFails)),
                arguments(FilterResult.BidAskFails, success, List.of(bidFails, askFails))
        );
    }
}
