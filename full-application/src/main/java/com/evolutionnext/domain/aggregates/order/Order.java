package com.evolutionnext.domain.aggregates.order;


import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.product.ProductId;
import com.evolutionnext.domain.events.order.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/// Aggregate for Order
public class Order {
    public static final String CANCELED_STATEMENT = "You can't submit a canceled order";
    private final OrderId orderId;
    private final List<OrderItem> orderItemList;
    private final List<OrderEvent> orderEventList;
    private final CustomerId customerId;


    protected Order(OrderId orderId, CustomerId customerId, ArrayList<OrderEvent> orderEventList) {
        this.orderId = orderId;
        this.orderItemList = new ArrayList<>();
        this.orderEventList = orderEventList;
        this.customerId = customerId;
    }

    public static Order of(OrderId orderId, CustomerId customer) {
        ArrayList<OrderEvent> orderEventList = new ArrayList<>();
        orderEventList.add(new OrderCreated(orderId, LocalDateTime.now()));
        return new Order(orderId, customer, orderEventList);
    }

    public void cancel() {
        orderEventList.add(new OrderCanceled(LocalDateTime.now()));
    }

    public void submit() {
        if (Objects.requireNonNull(getState()) instanceof OrderCanceled) {
            throw new IllegalStateException(CANCELED_STATEMENT);
        } else {
            orderEventList.add(new OrderSubmitted(LocalDateTime.now()));
        }
    }

    public void addOrderItem(ProductId productId, int quantity, int price) {
        OrderItem orderItem = new OrderItem(productId, quantity, price);
        orderItemList.add(orderItem);
        orderEventList.add(new OrderItemAdded(this.orderId, orderItem));
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public List<OrderEvent> getOrderEventList() {
        return orderEventList;
    }

    public OrderEvent getState() {
        return orderEventList.getLast();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public int total() {
        return orderItemList.stream()
            .mapToInt(item -> item.price() * item.quantity())
            .sum();
    }
}
