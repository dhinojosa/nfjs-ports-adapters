package com.evolutionnext.domain.services;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.Order;

public class OrderDomainService {

    /* In this section, I am using both a customer and an order aggregate */
    public static void checkCredit(Order order, Customer customer) {
        if (order.total() > customer.creditLimit()) {
            throw new IllegalStateException("Customer credit limit exceeded");
        }
    }
}
