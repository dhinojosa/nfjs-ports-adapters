package com.evolutionnext.domain.services;

import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.order.Order;

public class OrderDomainService {

    /* In this section, I am using both a customer and an order aggregate */
    public static boolean checkCredit(Order order, Customer customer) {
        return (order.total() > customer.creditLimit());
    }
}
