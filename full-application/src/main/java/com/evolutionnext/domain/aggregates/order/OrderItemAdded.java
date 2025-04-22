package com.evolutionnext.domain.aggregates.order;


public record OrderItemAdded(OrderId orderId, OrderItem orderItem) implements OrderEvent {
}
