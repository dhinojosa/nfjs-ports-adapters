package com.evolutionnext.application.results.customer.command;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CustomerCreated(CustomerId customerId) implements CustomerResult {
}
