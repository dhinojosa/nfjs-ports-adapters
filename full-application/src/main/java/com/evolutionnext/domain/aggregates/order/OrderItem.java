package com.evolutionnext.domain.aggregates.order;


import com.evolutionnext.domain.aggregates.product.ProductId;

import java.math.BigDecimal;

public record OrderItem(ProductId productId,
                        int quantity, BigDecimal price) {}
