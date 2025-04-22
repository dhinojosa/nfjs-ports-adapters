package com.evolutionnext.domain.aggregates.order;


import com.evolutionnext.domain.aggregates.product.ProductId;

public record OrderItem(ProductId productId,
                        int quantity, int price) {}
