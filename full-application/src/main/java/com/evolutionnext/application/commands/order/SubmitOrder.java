package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record SubmitOrder(OrderId orderId) implements OrderCommand {
}
