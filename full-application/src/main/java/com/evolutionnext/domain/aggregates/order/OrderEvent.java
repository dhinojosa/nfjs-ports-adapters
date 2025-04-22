package com.evolutionnext.domain.aggregates.order;


public sealed interface OrderEvent permits
    OrderCreated, OrderCanceled, OrderSubmitted, OrderItemAdded {
}
