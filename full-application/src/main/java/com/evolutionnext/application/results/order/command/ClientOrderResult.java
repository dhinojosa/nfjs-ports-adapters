package com.evolutionnext.application.results.order.command;


public sealed interface ClientOrderResult extends OrderResult permits OrderCanceled, OrderCreated, OrderItemAdded, OrderSubmitted {
}
