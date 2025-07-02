package com.evolutionnext.application.results.order.query;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CustomerNotFound(CustomerId customerId) implements OrderFindFailure {
}
