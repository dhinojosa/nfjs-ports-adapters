package com.evolutionnext.application.service.order;


import com.evolutionnext.application.port.in.order.ForClientOrderQueryPort;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.application.results.order.query.CustomerNotFound;
import com.evolutionnext.application.results.order.query.OrderFindResult;
import com.evolutionnext.application.results.order.query.OrderFindSuccess;
import com.evolutionnext.application.results.order.query.OrderNotFound;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.dto.order.OrderData;

public class OrderQueryApplicationService implements ForClientOrderQueryPort {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final Transactional transactional;

    public OrderQueryApplicationService (OrderRepository orderRepository,
                                         CustomerRepository customerRepository,
                                         Transactional transactional) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.transactional = transactional;
    }

    @Override
    public OrderFindResult findById(OrderId orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        return orderRepository.load(orderId)
            .map(order -> {
                CustomerId customerId = order.getCustomerId();
                return customerRepository.load(customerId)
                    .<OrderFindResult>map(customer -> new OrderFindSuccess(
                        new OrderData(order.getOrderId(), customerId,
                            customer.name(), order.total())))
                    .orElseGet(() -> new CustomerNotFound(customerId));
            })
            .orElseGet(() -> new OrderNotFound(orderId));
    }

}
