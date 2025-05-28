package com.evolutionnext.arbitraries;


import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import net.datafaker.Faker;
import net.jqwik.api.*;

import java.util.UUID;


public class CustomerArbitrarySupplier implements ArbitrarySupplier<Customer> {
    @Override
    public Arbitrary<Customer> get() {
        var creditLimit = Arbitraries.frequencyOf(
            Tuple.of(25, Arbitraries.integers().between(0, 99)),
            Tuple.of(50, Arbitraries.integers().between(100, 999)),
            Tuple.of(25, Arbitraries.integers().between(10_000, 50_000))
        );

        var names = Arbitraries.ofSuppliers(() -> new Faker().name().fullName());

        return Combinators.combine(
            Arbitraries.create(UUID::randomUUID),
            names,
            creditLimit
        ).as((uuid, name, limit) ->
            new Customer(new CustomerId(uuid), name, limit)
        );
    }
}
