package com.evolutionnext.dto.customer;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CustomerData(CustomerId customerId, String name, int numberOfOrders) {
}
