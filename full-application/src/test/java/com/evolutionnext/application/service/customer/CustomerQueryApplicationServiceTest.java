package com.evolutionnext.application.service.customer;

import com.evolutionnext.port.out.customer.CustomerRepository;
import com.evolutionnext.application.results.customer.query.CustomerFound;
import com.evolutionnext.application.results.customer.query.CustomerListFound;
import com.evolutionnext.application.results.customer.query.CustomerNotFound;
import com.evolutionnext.application.results.customer.query.CustomerQueryResult;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import net.jqwik.api.Example;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerQueryApplicationServiceTest {
    @Example
    void testFindAllWithNoReturn() {
        CustomerRepository customerRepositoryStub = new CustomerRepositoryStub();
        OrderRepositoryStub orderRepositoryStub = new OrderRepositoryStub();
        CustomerQueryApplicationService customerQueryApplicationService =
            new CustomerQueryApplicationService(customerRepositoryStub, orderRepositoryStub);
        CustomerQueryResult customerQueryResult = customerQueryApplicationService.findAll();
        assertThat(customerQueryResult).isInstanceOf(CustomerNotFound.class);
    }

    @Example
    void testFindByIdWithNoReturn() {
        CustomerRepository customerRepositoryStub = new CustomerRepositoryStub();
        OrderRepositoryStub orderRepositoryStub = new OrderRepositoryStub();
        CustomerQueryApplicationService customerQueryApplicationService =
            new CustomerQueryApplicationService(customerRepositoryStub, orderRepositoryStub);
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        CustomerQueryResult customerQueryResult = customerQueryApplicationService.findById(customerId);
        assertThat(customerQueryResult).isInstanceOf(CustomerNotFound.class);
    }

    @Example
    void testFindByIdWithValidReturn() {
        CustomerId barbaraId = new CustomerId(UUID.randomUUID());
        CustomerId micahId = new CustomerId(UUID.randomUUID());

        Customer customer1 = new Customer(barbaraId, "Barbara Lee", BigDecimal.valueOf(300));
        Customer customer2 = new Customer(micahId, "Micah Roberts", BigDecimal.valueOf(200));

        CustomerRepository customerRepositoryStub = new CustomerRepositoryStub();
        customerRepositoryStub.save(customer1);
        customerRepositoryStub.save(customer2);

        OrderRepositoryStub orderRepositoryStub = new OrderRepositoryStub();

        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), barbaraId));
        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), barbaraId));
        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), micahId));

        CustomerQueryApplicationService customerQueryApplicationService =
            new CustomerQueryApplicationService(customerRepositoryStub, orderRepositoryStub);
        CustomerQueryResult customerQueryResult = customerQueryApplicationService.findById(barbaraId);
        assertThat(customerQueryResult).isInstanceOf(CustomerFound.class);
        CustomerFound customerFound = (CustomerFound) customerQueryResult;
        assertThat(customerFound.customerData().numberOfOrders()).isEqualTo(2);
    }


    @Example
    void testFindByAllWithValidReturn() {
        CustomerId barbaraId = new CustomerId(UUID.randomUUID());
        CustomerId micahId = new CustomerId(UUID.randomUUID());

        Customer customer1 = new Customer(barbaraId, "Barbara Lee", BigDecimal.valueOf(300));
        Customer customer2 = new Customer(micahId, "Micah Roberts", BigDecimal.valueOf(200));

        CustomerRepository customerRepositoryStub = new CustomerRepositoryStub();
        customerRepositoryStub.save(customer1);
        customerRepositoryStub.save(customer2);

        OrderRepositoryStub orderRepositoryStub = new OrderRepositoryStub();

        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), barbaraId));
        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), barbaraId));
        orderRepositoryStub.save(Order.of(new OrderId(UUID.randomUUID()), micahId));

        CustomerQueryApplicationService customerQueryApplicationService =
            new CustomerQueryApplicationService(customerRepositoryStub, orderRepositoryStub);

        CustomerQueryResult customerQueryResult = customerQueryApplicationService.findAll();
        assertThat(customerQueryResult).isInstanceOf(CustomerListFound.class);
        CustomerListFound customerListFound = (CustomerListFound) customerQueryResult;

        assertThat(customerListFound.customerDataList()).hasSize(2);
        assertThat(customerListFound.customerDataList().get(0).numberOfOrders()).isGreaterThanOrEqualTo(1);
        assertThat(customerListFound.customerDataList().get(1).numberOfOrders()).isGreaterThanOrEqualTo(1);
    }
}
