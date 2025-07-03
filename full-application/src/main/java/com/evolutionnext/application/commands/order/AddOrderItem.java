package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.math.BigDecimal;

public record AddOrderItem(OrderId orderId, ProductId productId, int quantity, BigDecimal price) implements ClientOrderCommand {
}
