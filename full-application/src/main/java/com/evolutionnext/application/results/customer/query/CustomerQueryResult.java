package com.evolutionnext.application.results.customer.query;


public sealed interface CustomerQueryResult permits CustomerFound, CustomerNotFound, CustomerListFound {
}
