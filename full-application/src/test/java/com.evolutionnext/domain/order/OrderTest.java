package com.evolutionnext.domain.order;

import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.*;
import com.evolutionnext.domain.aggregates.product.ProductId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {
    @Test
    void testCreateOrderAggregate() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, 100);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, 10);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, 120);

        order.getOrderEventList().forEach(System.out::println);
    }

    @Test
    void testSubmittedOrderAggregate() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, 100);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, 10);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, 120);
        order.getOrderEventList().forEach(System.out::println);

        order.submit();
        OrderEvent state = order.getState();
        assertThat(state).isInstanceOf(OrderSubmitted.class);
    }

    @Test
    void testACanceledOrderCanNoLongerBeSubmitted() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, 100);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, 10);
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, 120);
        order.cancel();
        assertThatThrownBy(order::submit)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("You can't submit a canceled order");
    }
}
