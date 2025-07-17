package com.evolutionnext.application.service.customer;


import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepositoryStub implements OrderRepository {
    private final Map<OrderId, Order> orders = new HashMap<>();

    @Override
    public Optional<Order> load(OrderId orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public void delete(OrderId orderId) {
        orders.remove(orderId);
    }

    @Override
    public void deleteAll() {
        orders.clear();
    }

    @Override
    public int findCountByCustomer(CustomerId id) {
        return (int) orders.entrySet().stream().filter(e -> e.getValue().getCustomerId().equals(id)).count();
    }
}
