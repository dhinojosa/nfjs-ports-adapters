package com.evolutionnext.port.in.customer;


import com.evolutionnext.application.results.customer.query.CustomerQueryResult;

public interface ForAdminCustomerQueryPort extends ForClientCustomerQueryPort {
    public CustomerQueryResult findAll();
}
