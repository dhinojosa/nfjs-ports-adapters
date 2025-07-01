package com.evolutionnext.application.results.customer;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CustomerCreated(CustomerId customerId) implements CustomerResult {
}
