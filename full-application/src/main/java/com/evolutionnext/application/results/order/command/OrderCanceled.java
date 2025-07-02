package com.evolutionnext.application.results.order.command;

import com.evolutionnext.domain.aggregates.order.OrderId;

/*
 *  Cancel Order
 *   - DELETE http://localhost:8080/order/{orderId}
 *   - Response: { "orderId": "uuid"}
 */
public record OrderCanceled(OrderId orderId) implements ClientOrderResult, AdminOrderResult {}
