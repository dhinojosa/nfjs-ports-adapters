package com.evolutionnext.application.results.customer.query;


import java.util.List;

public record CustomerListFound(List<CustomerData> customerDataList) implements CustomerQueryResult {
}
