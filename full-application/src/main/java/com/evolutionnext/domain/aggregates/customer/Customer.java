package com.evolutionnext.domain.aggregates.customer;


public record Customer(CustomerId id, String name, double creditLimit) {
}
