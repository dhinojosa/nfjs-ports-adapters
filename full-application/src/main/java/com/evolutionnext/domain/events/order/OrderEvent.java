package com.evolutionnext.domain.events.order;


public sealed interface OrderEvent permits OrderCreated, OrderCanceled, OrderSubmitted, OrderItemAdded {
}
