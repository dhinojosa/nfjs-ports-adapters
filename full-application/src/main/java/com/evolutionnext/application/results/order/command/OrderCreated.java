package com.evolutionnext.application.results.order.command;


import com.evolutionnext.domain.aggregates.order.OrderId;

/**
 * This is for the result of the service and is not necessary what is in a DomainEvent
 * Initialize Order
 * - POST <a href="http://localhost:8080/order">...</a>
 * - Request: { "customerId": "uuid" }
 * - Response: { "orderId": "uuid" }
 */
public record OrderCreated(OrderId orderId) implements ClientOrderResult {
}
