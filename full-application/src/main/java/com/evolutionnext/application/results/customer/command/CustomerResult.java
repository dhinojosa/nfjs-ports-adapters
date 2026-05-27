package com.evolutionnext.application.results.customer.command;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public sealed interface CustomerResult permits CustomerResult.CustomerCreated {
    record CustomerCreated(CustomerId customerId) implements CustomerResult {
    }
}
