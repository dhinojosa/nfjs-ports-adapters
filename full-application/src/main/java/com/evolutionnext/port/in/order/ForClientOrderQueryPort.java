package com.evolutionnext.port.in.order;

import com.evolutionnext.application.results.order.query.OrderFindResult;
import com.evolutionnext.domain.aggregates.order.OrderId;

public interface ForClientOrderQueryPort {
    public OrderFindResult findById(OrderId id);
}
