package com.evolutionnext.arbitraries;


import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import net.datafaker.Faker;
import net.jqwik.api.*;

import java.util.UUID;

public class CustomerArbitrarySupplier implements ArbitrarySupplier<Customer> {
    @Override
    public Arbitrary<Customer> get() {
        Arbitrary<Integer> creditLimit = Arbitraries.frequencyOf(
            Tuple.of(25, Arbitraries.integers().between(0, 99)),          // 25%: Below 100
            Tuple.of(50, Arbitraries.integers().between(100, 999)),       // 50%: Between 100 and 1,000
            Tuple.of(25, Arbitraries.integers().between(10_000, 50_000))  // 25%: Above 10,000
        );

        // Build the full Customer Arbitrary
        return creditLimit.flatMap(limit ->
            Arbitraries.just(new Customer(
                new CustomerId(UUID.randomUUID()),   // Generate random UUID for CustomerId
                new Faker().name().fullName(),       // Generate realistic name
                limit                                // Use the generated credit limit
            ))
        );
    }
}
