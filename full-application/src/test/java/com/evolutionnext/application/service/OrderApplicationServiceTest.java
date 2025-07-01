package com.evolutionnext.application.service;

import com.evolutionnext.application.commands.order.AddOrderItem;
import com.evolutionnext.application.commands.order.CancelOrder;
import com.evolutionnext.application.commands.order.InitializeOrder;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.application.results.order.OrderCanceled;
import com.evolutionnext.application.results.order.OrderItemAdded;
import com.evolutionnext.application.results.order.OrderResult;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.domain.aggregates.product.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderApplicationServiceTest {
    private OrderRepository inMemoryOrderRepository;
    private CustomerRepository inMemoryCustomerRepository;
    private Transactional passthroughTransactional;

    @BeforeEach
    public void beforeEach() {
        inMemoryOrderRepository = new OrderRepository() {
            private final Map<OrderId, Order> orders = new HashMap<>();

            @Override
            public Optional<Order> load(OrderId orderId) {
                return Optional.ofNullable(orders.get(orderId));
            }

            @Override
            public void save(Order order) {
                orders.put(order.getOrderId(), order);
            }

            @Override
            public void delete(OrderId orderId) {
                orders.remove(orderId);
            }

            @Override
            public void deleteAll() {
                orders.clear();
            }
        };

        inMemoryCustomerRepository = new CustomerRepository() {
            private final Map<CustomerId, Customer> customers = new HashMap<>();

            @Override
            public Optional<Customer> load(CustomerId customerId) {
                return Optional.ofNullable(customers.get(customerId));
            }

            @Override
            public void save(Customer customer) {
                customers.put(customer.id(), customer);
            }

            @Override
            public List<Customer> findAll() {
                return new ArrayList<>(customers.values());
            }

            @Override
            public void delete(CustomerId customerId) {
                customers.remove(customerId);
            }

            @Override
            public void deleteAll() {
                customers.clear();
            }
        };

        passthroughTransactional = new Transactional() {
            @Override
            public <T> T transactionally(Supplier<T> work) {
                return work.get();
            }
        };
    }

    @Test
    void shouldInitializeOrderSuccessfully() {
        OrderApplicationService service = new
            OrderApplicationService(inMemoryOrderRepository,
            inMemoryCustomerRepository, passthroughTransactional);

        CustomerId customerId = new CustomerId(UUID.randomUUID());
        InitializeOrder command = new InitializeOrder(customerId);

        // Act
        OrderResult orderResult = service.execute(command);
        assertThat(orderResult).isNotNull();
    }

    @Test
    void shouldAddOrderItemSuccessfully() {
        // Arrange
        OrderApplicationService service = new
            OrderApplicationService(inMemoryOrderRepository,
            inMemoryCustomerRepository,
            passthroughTransactional);

        OrderId orderId = new OrderId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Order order = Order.of(orderId, customerId);
        inMemoryOrderRepository.save(order);

        ProductId productId = new ProductId(UUID.randomUUID());
        AddOrderItem command = new AddOrderItem(orderId, productId, 2, 100);

        // Act
        OrderResult result = service.execute(command);

        // Assert
        assertThat(result).isInstanceOf(OrderItemAdded.class);
        Optional<Order> loadedOrder = inMemoryOrderRepository.load(orderId);
        assertThat(loadedOrder).isPresent();
        assertThat(loadedOrder.get().total()).isEqualTo(200);
    }

    @Test
    void shouldThrowExceptionWhenAddingItemToNonExistentOrder() {
        // Arrange
        OrderApplicationService service = new OrderApplicationService(
            inMemoryOrderRepository,
            inMemoryCustomerRepository,
            passthroughTransactional);

        OrderId orderId = new OrderId(UUID.randomUUID());
        ProductId productId = new ProductId(UUID.randomUUID());
        AddOrderItem command = new AddOrderItem(orderId, productId, 2, 100);

        // Act & Assert
        assertThatThrownBy(() -> service.execute(command))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Order not found");
    }

    @Test
    void shouldAddMultipleOrderItemsSuccessfully() {
        // Arrange
        OrderApplicationService service = new OrderApplicationService(
            inMemoryOrderRepository,
            inMemoryCustomerRepository,
            passthroughTransactional);

        OrderId orderId = new OrderId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Order order = Order.of(orderId, customerId);
        inMemoryOrderRepository.save(order);

        ProductId productId1 = new ProductId(UUID.randomUUID());
        ProductId productId2 = new ProductId(UUID.randomUUID());
        AddOrderItem command1 = new AddOrderItem(orderId, productId1, 2, 100);
        AddOrderItem command2 = new AddOrderItem(orderId, productId2, 3, 50);

        // Act
        service.execute(command1);
        service.execute(command2);

        // Assert
        Optional<Order> loadedOrder = inMemoryOrderRepository.load(orderId);
        assertThat(loadedOrder).isPresent();
        assertThat(loadedOrder.get().total()).isEqualTo(350);
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        // Arrange
        OrderApplicationService service = new OrderApplicationService(
            inMemoryOrderRepository,
            inMemoryCustomerRepository,
            passthroughTransactional);

        OrderId orderId = new OrderId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Order order = Order.of(orderId, customerId);
        inMemoryOrderRepository.save(order);

        CancelOrder command = new CancelOrder(orderId);

        // Act
        OrderResult result = service.execute(command);

        // Assert
        assertThat(result).isInstanceOf(OrderCanceled.class);
        Optional<Order> loadedOrder = inMemoryOrderRepository.load(orderId);
        assertThat(loadedOrder).isPresent();
        assertThat(loadedOrder.get().getState()).isInstanceOf(com.evolutionnext.domain.events.order.OrderCanceled.class);
    }
}
