package com.evolutionnext.application.port.in.order;

import com.evolutionnext.application.results.order.query.OrderFindResult;
import com.evolutionnext.application.results.order.query.OrderFindSuccess;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.dto.customer.CustomerData;
import com.evolutionnext.dto.order.OrderData;

public interface ForClientOrderQueryPort {
    public OrderFindResult findById(OrderId id);
}
