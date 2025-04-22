package com.evolutionnext.domain.aggregates.order;


import java.time.LocalDateTime;

public record OrderCreated(OrderId orderId, LocalDateTime now) implements OrderEvent {
}
