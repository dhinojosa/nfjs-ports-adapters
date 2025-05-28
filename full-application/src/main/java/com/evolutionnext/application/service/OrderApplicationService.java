package com.evolutionnext.application.service;


import com.evolutionnext.application.port.in.*;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;

import java.util.UUID;

public class OrderApplicationService implements ForCustomerPort {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final Transactional transactional;

    public OrderApplicationService(OrderRepository orderRepository,
                                   CustomerRepository customerRepository,
                                   Transactional transactional) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.transactional = transactional;
    }

    @Override
    public void execute(OrderCommand command) {
        switch (command) {
            case InitializeOrder initializeOrder -> transactional.transactionally(() -> {
                Order order = Order.of(new OrderId(UUID.randomUUID()),
                    initializeOrder.customerId());
                orderRepository.save(order);
                return order.getOrderId();
            });
            case AddOrderItem addOrderItem -> transactional.transactionally(() -> {
                Order order = orderRepository.load(addOrderItem.orderId());
                order.addOrderItem(addOrderItem.productId(),
                    addOrderItem.quantity(),
                    addOrderItem.price());
                orderRepository.save(order);
                return order.getOrderId();
            });
            case CancelOrder cancelOrder -> transactional.transactionally(() -> {
                Order order = orderRepository.load(cancelOrder.orderId());
                order.cancel();
                orderRepository.save(order);
                return order.getOrderId();
            });
            case SubmitOrder submitOrder -> transactional.transactionally(() -> {
                Order order = orderRepository.load(submitOrder.orderId());
                order.submit();
                orderRepository.save(order);
                return order.getOrderId();
            });
        };
    }
}
