package com.evolutionnext.application.port.in;


public interface ForCustomerPort {
    void execute(OrderCommand command) throws Exception;
}
