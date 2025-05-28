package com.evolutionnext.arbitraries;


import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;
import net.datafaker.Faker;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;

import java.util.UUID;

public class ProductArbitrarySupplier implements ArbitrarySupplier<Product> {

    private static final Faker faker = new Faker();

    @Override
    public Arbitrary<Product> get() {
        return Arbitraries.floats().between(0.0f, 100.0f)
            .flatMap(price -> Arbitraries.create(UUID::randomUUID)
                .map(uuid -> new Product(new ProductId(uuid), faker.commerce().productName(), price)));
    }
}
