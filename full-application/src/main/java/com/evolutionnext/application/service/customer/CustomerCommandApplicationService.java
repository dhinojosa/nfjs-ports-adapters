package com.evolutionnext.application.service.customer;


import com.evolutionnext.application.commands.customer.CustomerCommand;
import com.evolutionnext.application.results.customer.command.CustomerCreated;
import com.evolutionnext.application.results.customer.command.CustomerResult;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.port.in.customer.ForClientCustomerCommandPort;
import com.evolutionnext.port.out.Transactional;
import com.evolutionnext.port.out.customer.CustomerRepository;

public class CustomerCommandApplicationService implements ForClientCustomerCommandPort {
    private final CustomerRepository customerRepository;
    private final Transactional transactional;

    public CustomerCommandApplicationService(CustomerRepository customerRepository,
                                             Transactional transactional) {
        this.customerRepository = customerRepository;
        this.transactional = transactional;
    }


    @Override
    public CustomerResult execute(CustomerCommand command) {
        return switch (command) {
            case CustomerCommand.CreateCustomer createCustomer -> transactional.transactionally(() -> {
                var customer = new Customer(createCustomer.customerId(),
                    createCustomer.name(),
                    createCustomer.creditLimit());
                customerRepository.save(customer);
                return new CustomerCreated(customer.id());
            });
        };
    }
}
