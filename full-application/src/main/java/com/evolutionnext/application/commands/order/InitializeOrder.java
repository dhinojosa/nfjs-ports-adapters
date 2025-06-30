package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record InitializeOrder(CustomerId customerId) implements OrderCommand { }
