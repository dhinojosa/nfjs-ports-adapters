package com.evolutionnext.application.results.order.command;


import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.order.OrderItem;

import java.util.List;

/**
 * This is a result message
 * Add Item to Cart
 * - POST http://localhost:8080/order/{orderId}/items
 * - Request: { "productId": "uuid", "quantity": number }
 * - Response: { "orderId": "uuid", "items": [] }
 */
public record OrderItemAdded(OrderId orderId, List<OrderItem> items) implements ClientOrderResult {
}
