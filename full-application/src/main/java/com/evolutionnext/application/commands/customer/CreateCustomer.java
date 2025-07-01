package com.evolutionnext.application.commands.customer;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

public record CreateCustomer(CustomerId customerId, String name, float creditLimit) implements CustomerCommand {
}
