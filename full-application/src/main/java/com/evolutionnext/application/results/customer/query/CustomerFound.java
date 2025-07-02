package com.evolutionnext.application.results.customer.query;


import com.evolutionnext.dto.customer.CustomerData;

public record CustomerFound(CustomerData customerData) implements CustomerQueryResult {
}
