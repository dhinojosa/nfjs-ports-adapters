package com.evolutionnext.domain.aggregates.order;


import com.evolutionnext.domain.aggregates.product.ProductId;

public record AddOrderItem(ProductId productId, int quantity, int price) implements OrderCommand {
}
