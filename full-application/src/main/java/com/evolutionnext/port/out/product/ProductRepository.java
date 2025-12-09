package com.evolutionnext.port.out.product;

import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> load(ProductId productId);
    void save(Product product);
    List<Product> findAll();
    void delete(ProductId productId);
    void deleteAll();
}
