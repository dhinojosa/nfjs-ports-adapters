package com.evolutionnext.application.results.order;

/*
 * Submit Order
 *    - POST http://localhost:8080/order/{orderId}/submit
 *    - Response: { "orderId": "uuid"}
 */


import com.evolutionnext.domain.aggregates.order.OrderId;

public record OrderSubmitted (OrderId orderId) implements OrderResult {}
