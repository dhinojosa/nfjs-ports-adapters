package com.evolutionnext.application.commands.order;


import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.math.BigDecimal;

public sealed interface ClientOrderCommand extends OrderCommand
    permits CancelOrder, ClientOrderCommand.AddOrderItem, ClientOrderCommand.InitializeOrder, SubmitOrder {

    public record AddOrderItem(OrderId orderId,
                               ProductId productId,
                               int quantity,
                               BigDecimal price) implements ClientOrderCommand {
    }
    public record InitializeOrder(CustomerId customerId) implements ClientOrderCommand { }
}

