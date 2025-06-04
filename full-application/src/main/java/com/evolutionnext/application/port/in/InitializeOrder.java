package com.evolutionnext.application.port.in;


import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;

public record InitializeOrder(CustomerId customerId) implements OrderCommand { }
