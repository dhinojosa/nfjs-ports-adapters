package com.evolutionnext.domain.aggregates.product;


import java.math.BigDecimal;

public record Product(ProductId id, String name, BigDecimal price) {
    public Product {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
    }
}
