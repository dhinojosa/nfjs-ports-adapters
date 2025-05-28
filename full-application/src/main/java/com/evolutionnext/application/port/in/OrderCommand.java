package com.evolutionnext.application.port.in;


public sealed interface OrderCommand permits InitializeOrder, AddOrderItem, SubmitOrder, CancelOrder {
}
