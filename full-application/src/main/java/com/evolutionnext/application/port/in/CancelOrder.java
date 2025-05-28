package com.evolutionnext.application.port.in;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record CancelOrder(OrderId orderId) implements OrderCommand {
}
