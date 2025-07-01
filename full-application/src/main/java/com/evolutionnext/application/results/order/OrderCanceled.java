package com.evolutionnext.application.results.order;

import com.evolutionnext.domain.aggregates.order.OrderId;

import java.util.UUID;

/*
 *  Cancel Order
 *   - DELETE http://localhost:8080/order/{orderId}
 *   - Response: { "orderId": "uuid"}
 */
public record OrderCanceled(OrderId orderId) implements OrderResult {}
