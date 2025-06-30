package com.evolutionnext.domain.events.order;


import com.evolutionnext.domain.aggregates.order.OrderId;

import java.time.LocalDateTime;

public record OrderCreated(OrderId orderId, LocalDateTime now) implements OrderEvent {
}
