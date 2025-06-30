package com.evolutionnext.domain.events.order;


import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.order.OrderItem;

public record OrderItemAdded(OrderId orderId, OrderItem orderItem) implements OrderEvent {
}
