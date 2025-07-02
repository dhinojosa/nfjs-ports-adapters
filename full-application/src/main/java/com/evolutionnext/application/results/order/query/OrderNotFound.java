package com.evolutionnext.application.results.order.query;

import com.evolutionnext.domain.aggregates.order.OrderId;

public record OrderNotFound(OrderId orderId) implements OrderFindFailure {}
