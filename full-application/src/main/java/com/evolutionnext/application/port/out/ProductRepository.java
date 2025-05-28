package com.evolutionnext.application.port.out;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.util.List;

public interface ProductRepository {
    Product load(ProductId productId);

    void save(Product product);

    List<Product> findAll();

    void delete(ProductId productId);

    void deleteAll();
}
