package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record FulfillOrder(OrderId orderId) implements AdminOrderCommand {
}
