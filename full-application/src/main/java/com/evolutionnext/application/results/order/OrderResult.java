package com.evolutionnext.application.results.order;


public sealed interface OrderResult permits OrderCreated, OrderItemAdded, OrderSubmitted, OrderCanceled { }
