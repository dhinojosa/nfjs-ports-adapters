package com.evolutionnext.application.results.order.command;


public sealed interface OrderResult permits ClientOrderResult, AdminOrderResult { }
