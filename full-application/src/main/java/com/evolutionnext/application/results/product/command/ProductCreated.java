package com.evolutionnext.application.results.product.command;


import com.evolutionnext.domain.aggregates.product.ProductId;

public record ProductCreated(ProductId productId) implements ProductResult {
}
