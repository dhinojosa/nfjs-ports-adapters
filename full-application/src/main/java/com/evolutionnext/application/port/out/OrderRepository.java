package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

public interface OrderRepository {
    public void submit(Order order);
    public Order getById(OrderId orderId);
}


