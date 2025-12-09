package com.evolutionnext.port.out.order;

import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> load(OrderId orderId);
    void save(Order order);
    void delete(OrderId orderId);
    void deleteAll();
    int findCountByCustomer(CustomerId id);
}


