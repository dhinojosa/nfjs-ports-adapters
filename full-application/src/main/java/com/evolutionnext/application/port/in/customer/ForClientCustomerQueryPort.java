package com.evolutionnext.application.port.in.customer;

import com.evolutionnext.application.results.customer.query.CustomerQueryResult;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

public interface ForClientCustomerQueryPort {
    public CustomerQueryResult findById(CustomerId id);
}
