package com.evolutionnext.application.commands.customer;


import com.evolutionnext.domain.aggregates.customer.CustomerId;

import java.math.BigDecimal;

public sealed interface CustomerCommand permits CustomerCommand.CreateCustomer {
    public record CreateCustomer(CustomerId customerId, String name, BigDecimal creditLimit) implements CustomerCommand {
    }
}
