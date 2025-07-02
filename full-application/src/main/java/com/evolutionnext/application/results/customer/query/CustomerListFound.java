package com.evolutionnext.application.results.customer.query;


import com.evolutionnext.dto.customer.CustomerData;

import java.util.List;

public record CustomerListFound(List<CustomerData> customerDataList) implements CustomerQueryResult {
}
