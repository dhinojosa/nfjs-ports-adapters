package com.evolutionnext.application.results.order.query;

import com.evolutionnext.dto.order.OrderData;

/**
 * Represents the successful result of finding an order.
 * Contains the found order.
 */
public record OrderFindSuccess(OrderData orderData) implements OrderFindResult {
}
