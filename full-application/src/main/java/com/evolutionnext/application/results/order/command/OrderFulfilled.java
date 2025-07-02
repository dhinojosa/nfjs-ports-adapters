package com.evolutionnext.application.results.order.command;


import com.evolutionnext.domain.aggregates.order.OrderId;

public record OrderFulfilled(OrderId orderId) implements AdminOrderResult {
}
