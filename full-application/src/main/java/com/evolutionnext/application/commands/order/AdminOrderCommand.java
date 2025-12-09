package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;

public sealed interface
AdminOrderCommand extends OrderCommand permits AdminOrderCommand.FulfillOrder, CancelOrder {
    public record FulfillOrder(OrderId orderId) implements AdminOrderCommand {
    }
}
