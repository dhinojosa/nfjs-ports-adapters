package com.evolutionnext.application.commands.order;


public sealed interface AdminOrderCommand permits InitializeOrder, FulfillOrder {
}
