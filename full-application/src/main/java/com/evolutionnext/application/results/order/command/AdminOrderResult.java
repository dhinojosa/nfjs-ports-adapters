package com.evolutionnext.application.results.order.command;


public sealed interface AdminOrderResult extends OrderResult permits OrderCanceled, OrderFulfilled {
}
