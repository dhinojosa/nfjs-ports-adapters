package com.evolutionnext.application;


import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.AddOrderItem;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.order.OrderItem;
import com.evolutionnext.domain.aggregates.product.ProductId;
import com.evolutionnext.domain.services.OrderDomainService;

public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderApplicationService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public void createOrder(Order order) {
        Customer customer = customerRepository.getCustomer(order.getCustomerId());
        OrderDomainService.checkCredit(order, customer);
        //transaction
        orderRepository.submit(order);
        //commit transaction
        order.getOrderEventList().forEach(System.out::println);
    }

    public void addOrderItem(OrderId orderId, OrderItem orderItem) {
        Order order = orderRepository.getById(orderId);
        order.execute(new AddOrderItem(new ProductId(120L), 20, 1120));
    }
}
