package com.evolutionnext.application.commands.order;


public sealed interface OrderCommand permits AddOrderItem, CancelOrder, InitializeOrder, SubmitOrder {
}
