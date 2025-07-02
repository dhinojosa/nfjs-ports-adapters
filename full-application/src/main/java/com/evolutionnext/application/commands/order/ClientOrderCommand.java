package com.evolutionnext.application.commands.order;


public sealed interface ClientOrderCommand extends OrderCommand permits CancelOrder, AddOrderItem, InitializeOrder, SubmitOrder {}

