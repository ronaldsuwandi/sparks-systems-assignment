package com.ronaldsuwandi.filters;

class FilterResultHelper {
    static FilterResult getFilterResult(boolean bidFail, boolean askFail) {
        if (!bidFail && !askFail) {
            return FilterResult.Success;
        } else {
            if (bidFail && askFail) {
                return FilterResult.BidAskFails;
            } else if (bidFail) {
                return FilterResult.BidFails;
            } else {
                return FilterResult.AskFails;
            }
        }
    }
}
