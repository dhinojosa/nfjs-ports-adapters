package com.evolutionnext.arbitraries;


import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;
import net.datafaker.Faker;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.Provide;

import java.util.UUID;

public class ProductArbitrarySupplier implements ArbitrarySupplier<Product> {

    private static final Faker faker = new Faker();

    public static Arbitrary<UUID> arbitraryUUID() {
        return Arbitraries.create(UUID::randomUUID);
    }

    @Override
    public Arbitrary<Product> get() {
        return Arbitraries.floats().between(0.0f, 100.0f)
            .flatMap(price -> arbitraryUUID()
                .map(uuid -> new Product(new ProductId(uuid), faker.commerce().productName(), price)));
    }
}
