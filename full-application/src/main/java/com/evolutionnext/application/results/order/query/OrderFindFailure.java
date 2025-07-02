package com.evolutionnext.application.results.order.query;

/**
 * Represents a failure that occurred during an attempt to find an order.
 */
public sealed interface OrderFindFailure extends OrderFindResult
    permits OrderNotFound, CustomerNotFound {
}
