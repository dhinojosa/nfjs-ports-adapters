package com.evolutionnext.domain.aggregates.order;


public sealed interface OrderCommand permits AddOrderItem, SubmitOrder, CancelOrder {
}
