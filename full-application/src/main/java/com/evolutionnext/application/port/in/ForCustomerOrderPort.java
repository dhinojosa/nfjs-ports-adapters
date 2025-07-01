package com.evolutionnext.application.port.in;


import com.evolutionnext.application.commands.order.OrderCommand;
import com.evolutionnext.application.results.order.OrderResult;

public interface ForCustomerOrderPort {
    OrderResult execute(OrderCommand command) throws Exception;
}
