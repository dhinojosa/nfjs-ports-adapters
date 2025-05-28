package com.evolutionnext.application.port.in;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record SubmitOrder(OrderId orderId) implements OrderCommand {
}
