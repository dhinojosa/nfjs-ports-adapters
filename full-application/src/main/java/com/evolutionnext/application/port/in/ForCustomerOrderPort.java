package com.evolutionnext.application.port.in;


import com.evolutionnext.application.commands.order.OrderCommand;
import com.evolutionnext.domain.aggregates.order.OrderId;

public interface ForCustomerOrderPort {
    OrderId execute(OrderCommand command) throws Exception;
}
