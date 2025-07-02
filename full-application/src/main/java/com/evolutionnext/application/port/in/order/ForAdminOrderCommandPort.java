package com.evolutionnext.application.port.in.order;


import com.evolutionnext.application.commands.order.AdminOrderCommand;
import com.evolutionnext.application.results.order.command.AdminOrderResult;

public interface ForAdminOrderCommandPort {
    AdminOrderResult execute(AdminOrderCommand command);
}
