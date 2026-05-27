package com.evolutionnext.application.results.customer.query;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CustomerData(CustomerId customerId, String name, int numberOfOrders) {
}
