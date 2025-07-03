package com.evolutionnext.domain.aggregates.customer;


import java.math.BigDecimal;

public record Customer(CustomerId id, String name, BigDecimal creditLimit) {
}
