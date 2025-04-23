package com.evolutionnext;

import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.google.inject.AbstractModule;

public class CucumberModules extends AbstractModule {
    @Override
    protected void configure() {
        bind(Order.class).toInstance(Order.of(new OrderId(3L), new CustomerId(30L)));
    }
}
