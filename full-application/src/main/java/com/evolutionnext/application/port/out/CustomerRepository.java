package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

import java.util.List;

public interface CustomerRepository {
    Customer load(CustomerId customerId);
    void save(Customer customer);
    List<Customer> findAll();
    void delete(CustomerId customerId);
    void deleteAll();
}
