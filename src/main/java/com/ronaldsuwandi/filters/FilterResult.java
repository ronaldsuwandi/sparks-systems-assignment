package com.ronaldsuwandi.filters;

// Indicates the result after applying filter, it can be either successful, or fails (if it fails, it can be either
// the bid value fails, ask value fails or both)
public enum FilterResult {
    Success,
    BidFails,
    AskFails,
    BidAskFails,
}
