package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record CancelOrder(OrderId orderId) implements OrderCommand {
}
