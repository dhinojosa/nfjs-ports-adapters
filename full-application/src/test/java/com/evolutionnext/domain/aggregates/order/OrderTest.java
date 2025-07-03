package com.evolutionnext.domain.aggregates.order;

import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.product.ProductId;
import com.evolutionnext.domain.events.order.OrderEvent;
import com.evolutionnext.domain.events.order.OrderSubmitted;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTest {
    private static final Logger logger = LoggerFactory.getLogger(OrderTest.class);

    @Test
    void testCreateOrderAggregate() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));

        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, BigDecimal.valueOf(100));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, BigDecimal.valueOf(10));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, BigDecimal.valueOf(120));

        order.getOrderEventList().forEach(e -> logger.debug(e.toString()));
    }

    @Test
    void testSubmittedOrderAggregate() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, BigDecimal.valueOf(100));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, BigDecimal.valueOf(10));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, BigDecimal.valueOf(120));
        List<OrderEvent> orderEventList = order.getOrderEventList();
        assertThat(orderEventList.size()).isEqualTo(4);

        order.submit();
        OrderEvent state = order.getState();
        assertThat(state).isInstanceOf(OrderSubmitted.class);
    }

    @Test
    void testACanceledOrderCanNoLongerBeSubmitted() {
        Order order = Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID()));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 10, BigDecimal.valueOf(100));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 4, BigDecimal.valueOf(10));
        order.addOrderItem(new ProductId(UUID.randomUUID()), 1, BigDecimal.valueOf(120));
        order.cancel();
        assertThatThrownBy(order::submit)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("You can't submit a canceled order");
    }
}
