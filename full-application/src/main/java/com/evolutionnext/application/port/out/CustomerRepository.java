package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

public interface CustomerRepository {
    Customer getCustomer(CustomerId customerId);
}
