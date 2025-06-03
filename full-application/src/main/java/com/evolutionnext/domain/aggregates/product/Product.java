package com.evolutionnext.domain.aggregates.product;


import java.math.BigDecimal;

public record Product(ProductId id, String name, BigDecimal price) {
}
