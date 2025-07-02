package com.evolutionnext.dto.order;


import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;

public record OrderData(OrderId orderId, CustomerId customerId, String name, float total) {

}
