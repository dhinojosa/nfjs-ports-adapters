package com.evolutionnext.application.port.in;


import com.evolutionnext.application.commands.customer.CustomerCommand;
import com.evolutionnext.application.results.customer.CustomerResult;

public interface ForCustomerCustomerPort {
    CustomerResult execute(CustomerCommand command) throws Exception;
}
