package com.evolutionnext.application.results.product;


import com.evolutionnext.domain.aggregates.product.ProductId;

public record ProductCreated(ProductId productId) implements ProductResult {
}
