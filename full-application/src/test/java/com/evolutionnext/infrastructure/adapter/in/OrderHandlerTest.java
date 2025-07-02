package com.evolutionnext.infrastructure.adapter.in;

import com.evolutionnext.application.commands.order.*;
import com.evolutionnext.application.port.in.order.ForClientOrderCommandPort;
import com.evolutionnext.application.results.order.command.OrderCanceled;
import com.evolutionnext.application.results.order.command.OrderCreated;
import com.evolutionnext.application.results.order.command.OrderItemAdded;
import com.evolutionnext.application.results.order.command.OrderSubmitted;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.infrastructure.adapter.HTTPExchangeStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderHandlerTest.class);

    private ObjectMapper objectMapper;
    private List<OrderCommand> recordedOrders;
    private ForClientOrderCommandPort forCustomerPort;
    private ByteArrayOutputStream responseOutputStream;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        recordedOrders = new ArrayList<>();
        forCustomerPort = command -> {
            recordedOrders.add(command);
            return switch (command) {
                case AddOrderItem addOrderItem -> new OrderItemAdded(addOrderItem.orderId(), List.of());
                case CancelOrder cancelOrder -> new OrderCanceled(cancelOrder.orderId());
                case InitializeOrder _ -> new OrderCreated(new OrderId(UUID.randomUUID()));
                case SubmitOrder submitOrder -> new OrderSubmitted(submitOrder.orderId());
            };
        };
    }

    @Test
    void testInitializerOrder() throws IOException {
        OrderHandler orderHandler = new OrderHandler(forCustomerPort, objectMapper);
        CustomerId customerId = new CustomerId(UUID.randomUUID());

        String requestPayload = """
            {
                "customerId": "%s"
            }
            """.formatted(customerId.id().toString());

        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

        var httpExchange = HTTPExchangeStub.builder()
            .setRequestMethod("POST")
            .setRequestBody(new ByteArrayInputStream(requestPayload.getBytes()))
            .setResponseBody(responseOutputStream)
            .build();

        orderHandler.handle(httpExchange);

        assertThat(recordedOrders).hasSize(1);

        OrderCommand recordedOrder = recordedOrders.getFirst();
        assertThat(recordedOrder).isInstanceOf(InitializeOrder.class);
        assertThat(recordedOrder).extracting("customerId").isEqualTo(customerId);

        String string = responseOutputStream.toString(Charset.defaultCharset());
        logger.debug("Return string {}", string);

    }
}
