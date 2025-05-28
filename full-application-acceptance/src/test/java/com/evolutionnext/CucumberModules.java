package com.evolutionnext;

import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.google.inject.AbstractModule;

import java.util.UUID;

public class CucumberModules extends AbstractModule {
    @Override
    protected void configure() {
        bind(Order.class).toInstance(Order.of(new OrderId(UUID.randomUUID()), new CustomerId(UUID.randomUUID())));
    }
}
