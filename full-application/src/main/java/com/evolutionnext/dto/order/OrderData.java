package com.evolutionnext.dto.order;


import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;

import java.math.BigDecimal;

public record OrderData(OrderId orderId, CustomerId customerId, String name, BigDecimal total) {

}
