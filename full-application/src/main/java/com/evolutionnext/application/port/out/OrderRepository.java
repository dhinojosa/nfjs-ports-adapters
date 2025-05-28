package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

public interface OrderRepository {
    Order load(OrderId orderId);

    void save(Order order);

    void delete(OrderId orderId);

    void deleteAll();
}


