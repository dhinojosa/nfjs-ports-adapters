package com.evolutionnext.application.port.in.order;


import com.evolutionnext.application.commands.order.ClientOrderCommand;
import com.evolutionnext.application.results.order.command.ClientOrderResult;

public interface ForClientOrderCommandPort {
    ClientOrderResult execute(ClientOrderCommand command) throws Exception;
}
