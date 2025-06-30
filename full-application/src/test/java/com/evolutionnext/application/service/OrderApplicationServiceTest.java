package com.evolutionnext.application.service;

import com.evolutionnext.application.commands.order.AddOrderItem;
import com.evolutionnext.application.commands.order.CancelOrder;
import com.evolutionnext.application.commands.order.InitializeOrder;
import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.application.port.out.Transactional;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.events.order.OrderCanceled;
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
        OrderId result = service.execute(command);
        assertThat(result).isNotNull();
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
        OrderId result = service.execute(command);

        // Assert
        assertThat(result).isEqualTo(orderId);
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
        OrderId result = service.execute(command);

        // Assert
        assertThat(result).isEqualTo(orderId);
        Optional<Order> loadedOrder = inMemoryOrderRepository.load(orderId);
        assertThat(loadedOrder).isPresent();
        assertThat(loadedOrder.get().getState()).isInstanceOf(OrderCanceled.class);
    }


//    @Test
//    void shouldCancelOrderSuccessfully() {
//        // Arrange
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        CustomerRepository customerRepository = mock(CustomerRepository.class);
//        Transactional transactional = mock(Transactional.class);
//        OrderApplicationService service = new OrderApplicationService(orderRepository, customerRepository, transactional);
//
//        UUID orderIdValue = UUID.randomUUID();
//        OrderId orderId = new OrderId(orderIdValue);
//        Order existingOrder = mock(Order.class);
//
//        OrderCommand.CancelOrder command = new OrderCommand.CancelOrder(orderId);
//
//        when(orderRepository.load(orderId)).thenReturn(Optional.of(existingOrder));
//        when(transactional.transactionally(any())).thenAnswer(invocation -> {
//            var transactionalFunction = invocation.getArgument(0, Transactional.TransactionalFunction.class);
//            return transactionalFunction.apply();
//        });
//
//        // Act
//        OrderId result = service.execute(command);
//
//        // Assert
//        assertEquals(orderId, result);
//        verify(existingOrder, times(1)).cancel();
//        verify(orderRepository, times(1)).save(existingOrder);
//    }
//
//    @Test
//    void shouldSubmitOrderSuccessfully() {
//        // Arrange
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        CustomerRepository customerRepository = mock(CustomerRepository.class);
//        Transactional transactional = mock(Transactional.class);
//        OrderApplicationService service = new OrderApplicationService(orderRepository, customerRepository, transactional);
//
//        UUID orderIdValue = UUID.randomUUID();
//        OrderId orderId = new OrderId(orderIdValue);
//        UUID customerId = UUID.randomUUID();
//        Order order = mock(Order.class);
//        Customer customer = mock(Customer.class);
//
//        OrderCommand.SubmitOrder command = new OrderCommand.SubmitOrder(orderId);
//
//        when(orderRepository.load(orderId)).thenReturn(Optional.of(order));
//        when(customerRepository.load(customerId)).thenReturn(Optional.of(customer));
//        when(transactional.transactionally(any())).thenAnswer(invocation -> {
//            var transactionalFunction = invocation.getArgument(0, Transactional.TransactionalFunction.class);
//            return transactionalFunction.apply();
//        });
//        when(order.getCustomerId()).thenReturn(customerId);
//        when(OrderDomainService.checkCredit(order, customer)).thenReturn(true);
//
//        // Act
//        OrderId result = service.execute(command);
//
//        // Assert
//        assertEquals(orderId, result);
//        verify(order, times(1)).submit();
//        verify(orderRepository, times(1)).save(order);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenInsufficientCreditForSubmitOrder() {
//        // Arrange
//        OrderRepository orderRepository = mock(OrderRepository.class);
//        CustomerRepository customerRepository = mock(CustomerRepository.class);
//        Transactional transactional = mock(Transactional.class);
//        OrderApplicationService service = new OrderApplicationService(orderRepository, customerRepository, transactional);
//
//        UUID orderIdValue = UUID.randomUUID();
//        OrderId orderId = new OrderId(orderIdValue);
//        UUID customerId = UUID.randomUUID();
//        Order order = mock(Order.class);
//        Customer customer = mock(Customer.class);
//
//        OrderCommand.SubmitOrder command = new OrderCommand.SubmitOrder(orderId);
//
//        when(orderRepository.load(orderId)).thenReturn(Optional.of(order));
//        when(customerRepository.load(customerId)).thenReturn(Optional.of(customer));
//        when(transactional.transactionally(any())).thenAnswer(invocation -> {
//            var transactionalFunction = invocation.getArgument(0, Transactional.TransactionalFunction.class);
//            return transactionalFunction.apply();
//        });
//        when(order.getCustomerId()).thenReturn(customerId);
//        when(OrderDomainService.checkCredit(order, customer)).thenReturn(false);
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.execute(command));
//        assertEquals("Insufficient credit", exception.getMessage());
//    }
}
