package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.product.ProductId;

public record AddOrderItem(OrderId orderId, ProductId productId, int quantity, int price) implements OrderCommand {
}
