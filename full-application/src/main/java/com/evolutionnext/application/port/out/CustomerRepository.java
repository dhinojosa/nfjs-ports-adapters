package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> load(CustomerId customerId);
    void save(Customer customer);
    List<Customer> findAll();
    void delete(CustomerId customerId);
    void deleteAll();
}
