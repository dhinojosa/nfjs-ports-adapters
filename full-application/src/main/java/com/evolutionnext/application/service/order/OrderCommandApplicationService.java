package com.evolutionnext.application.service.order;


import com.evolutionnext.application.commands.order.*;
import com.evolutionnext.application.port.in.order.ForAdminOrderCommandPort;
import com.evolutionnext.application.port.in.order.ForClientOrderCommandPort;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.application.results.order.command.*;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.services.OrderDomainService;

import java.util.UUID;

public class OrderCommandApplicationService implements ForClientOrderCommandPort, ForAdminOrderCommandPort {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final Transactional transactional;

    public OrderCommandApplicationService(OrderRepository orderRepository,
                                          CustomerRepository customerRepository,
                                          Transactional transactional) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.transactional = transactional;
    }

    @Override
    public ClientOrderResult execute(ClientOrderCommand command) {
        return switch (command) {
            case InitializeOrder initializeOrder -> transactional.transactionally(() -> {
                Order order = Order.of(new OrderId(UUID.randomUUID()),
                    initializeOrder.customerId());
                orderRepository.save(order);
                return new OrderSubmitted(order.getOrderId());
            });
            case AddOrderItem addOrderItem -> transactional.transactionally(() -> {
                Order order = orderRepository.load(addOrderItem.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
                order.addOrderItem(addOrderItem.productId(),
                    addOrderItem.quantity(),
                    addOrderItem.price());
                orderRepository.save(order);
                return new OrderItemAdded(order.getOrderId(), order.getOrderItemList());
            });
            case CancelOrder cancelOrder -> transactional.transactionally(() -> {
                Order order = orderRepository.load(cancelOrder.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
                order.cancel();
                orderRepository.save(order);
                return new OrderCanceled(order.getOrderId());
            });
            case SubmitOrder submitOrder -> transactional.transactionally(() -> {
                Order order = orderRepository.load(submitOrder.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
                Customer customer = customerRepository.load(order.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
                if (!OrderDomainService.checkCredit(order, customer)) {
                    throw new RuntimeException("Insufficient credit");
                }
                order.submit();
                orderRepository.save(order);
                return new OrderSubmitted(order.getOrderId());
            });
        };
    }

    @Override
    public AdminOrderResult execute(AdminOrderCommand command) {
        return switch (command) {
            case CancelOrder cancelOrder -> {
                Order order = orderRepository.load(cancelOrder.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
                order.cancel();
                orderRepository.save(order);
                yield new OrderCanceled(order.getOrderId());
            }
            case FulfillOrder fulfillOrder -> {
                Order order = orderRepository.load(fulfillOrder.orderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
                order.fulfill();
                orderRepository.save(order);
                yield new OrderFulfilled(order.getOrderId());
            }
        };
    }
}
