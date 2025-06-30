package com.evolutionnext.application.commands.order;


public sealed interface OrderCommand permits InitializeOrder, AddOrderItem, SubmitOrder, CancelOrder {
}
