package com.evolutionnext.application.service.product;

import com.evolutionnext.application.commands.product.CreateProduct;
import com.evolutionnext.application.commands.product.ProductCommand;
import com.evolutionnext.application.port.in.product.ForAdminProductCommandPort;
import com.evolutionnext.application.port.out.ProductRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.application.results.product.command.ProductCreated;
import com.evolutionnext.application.results.product.command.ProductResult;
import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductCommandApplicationService implements ForAdminProductCommandPort {

    private final ProductRepository productRepository;
    private final Transactional transactional;

    public ProductCommandApplicationService(ProductRepository productRepository, Transactional transactional) {
        this.productRepository = productRepository;
        this.transactional = transactional;
    }

    @Override
    public ProductResult execute(ProductCommand command) throws Exception {
        return switch (command) {
            case CreateProduct(String name, BigDecimal price) -> transactional.transactionally(() -> {
                ProductId id = new ProductId(UUID.randomUUID());
                productRepository.save(new Product(id, name, price));
                return new ProductCreated(id);
            });
        };
    }
}
