package com.evolutionnext.application.port.in;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record InitializeOrder(CustomerId customerId) implements OrderCommand { }
