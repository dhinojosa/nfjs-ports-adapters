package com.evolutionnext.application.commands.order;


public sealed interface AdminOrderCommand extends OrderCommand permits CancelOrder, FulfillOrder {
}
