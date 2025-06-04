package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> load(OrderId orderId);

    void save(Order order);

    void delete(OrderId orderId);

    void deleteAll();
}


