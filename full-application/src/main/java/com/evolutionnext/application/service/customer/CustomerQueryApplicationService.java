package com.evolutionnext.application.service.customer;


import com.evolutionnext.application.port.in.customer.ForAdminCustomerQueryPort;
import com.evolutionnext.application.port.in.customer.ForClientCustomerQueryPort;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.results.customer.query.CustomerFound;
import com.evolutionnext.application.results.customer.query.CustomerListFound;
import com.evolutionnext.application.results.customer.query.CustomerNotFound;
import com.evolutionnext.application.results.customer.query.CustomerQueryResult;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.dto.customer.CustomerData;

import java.util.List;

public class CustomerQueryApplicationService implements ForAdminCustomerQueryPort, ForClientCustomerQueryPort {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerQueryApplicationService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public CustomerQueryResult findAll() {
        List<CustomerData> customers = customerRepository.findAll()
            .stream()
            .map(customer -> new CustomerData(customer.id(), customer.name(),
                orderRepository.findCountByCustomer(customer.id())))
            .toList();
        return customers.isEmpty()
            ? new CustomerNotFound()
            : new CustomerListFound(customers);
    }

    @Override
    public CustomerQueryResult findById(CustomerId id) {
        return customerRepository.load(id)
            .<CustomerQueryResult>map(customer -> new CustomerFound(
                new CustomerData(customer.id(),
                    customer.name(),
                    orderRepository.findCountByCustomer(customer.id()))))
            .orElseGet(CustomerNotFound::new);
    }
}
