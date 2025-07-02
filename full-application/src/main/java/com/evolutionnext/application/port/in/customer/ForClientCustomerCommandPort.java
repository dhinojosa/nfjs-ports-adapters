package com.evolutionnext.application.port.in.customer;


import com.evolutionnext.application.commands.customer.CustomerCommand;
import com.evolutionnext.application.results.customer.command.CustomerResult;

public interface ForClientCustomerCommandPort {
    CustomerResult execute(CustomerCommand command);
}
