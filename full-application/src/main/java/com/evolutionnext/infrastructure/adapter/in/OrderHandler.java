package com.evolutionnext.infrastructure.adapter.in;


import com.evolutionnext.application.commands.order.ClientOrderCommand;
import com.evolutionnext.application.results.order.command.*;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.Order;
import com.evolutionnext.domain.aggregates.order.OrderId;
import com.evolutionnext.port.in.order.ForClientOrderCommandPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class OrderHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    private final ForClientOrderCommandPort forCustomerPort;
    private final ObjectMapper objectMapper;

    public OrderHandler(ForClientOrderCommandPort forCustomerPort,
                        ObjectMapper objectMapper) {
        this.forCustomerPort = forCustomerPort;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                InputStream requestBody = exchange.getRequestBody();
                OrderRequestDTO orderRequest =
                    objectMapper.readValue(requestBody, OrderRequestDTO.class);

                Order order = Order.of(new OrderId(UUID.randomUUID()),
                    new CustomerId(UUID.fromString(orderRequest.customerId)));

                ClientOrderCommand.InitializeOrder initializeOrder =
                    new ClientOrderCommand.InitializeOrder(new CustomerId(UUID.fromString(orderRequest.customerId)));

                logger.debug("Executing command {}", initializeOrder);
                OrderResult orderResult = forCustomerPort.execute(initializeOrder);

                switch (orderResult) {
                    case AdminOrderResult adminOrderResult -> {
                        switch (adminOrderResult) {
                            case OrderCanceled orderCanceled -> {
                            }
                            case OrderFulfilled orderFulfilled -> {
                            }
                        }
                    }
                    case ClientOrderResult clientOrderResult -> {
                    }
                }
                logger.debug("Executed command {} and received id {}", initializeOrder, orderResult);

                String response = objectMapper.writeValueAsString(order);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, response.length());

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                exchange.sendResponseHeaders(400, errorMessage.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorMessage.getBytes());
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
