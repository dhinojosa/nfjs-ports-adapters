package com.evolutionnext.application.service.customer;


import com.evolutionnext.port.out.customer.CustomerRepository;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepositoryStub implements CustomerRepository {
    private final Map<CustomerId, Customer> customers = new HashMap<>();

    @Override
    public Optional<Customer> load(CustomerId customerId) {

        return Optional.ofNullable(customers.get(customerId));
    }

    @Override
    public void save(Customer customer) {
        customers.put(customer.id(), customer);
    }

    @Override
    public List<Customer> findAll() {
        return customers.values().stream().toList();
    }

    @Override
    public void delete(CustomerId customerId) {
        customers.remove(customerId);
    }

    @Override
    public void deleteAll() {
        customers.clear();
    }
}
