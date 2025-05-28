package com.evolutionnext.infrastructure.adapter.out;


import com.evolutionnext.application.port.out.OrderRepository;
import com.evolutionnext.domain.aggregates.customer.CustomerId;
import com.evolutionnext.domain.aggregates.order.*;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OrderJDBCRepository implements OrderRepository {

    @Override
    public Order load(OrderId orderId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM orders WHERE id = ?");
            ps.setString(1, orderId.value().toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Order.of(new OrderId(UUID.fromString(rs.getString("id"))),
                    new CustomerId(UUID.fromString(rs.getString("customer_id"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Order not found");
    }

    @Override
    public void save(Order order){
        persistOrder(order);
        persistOrderItems(order);
    }

    @Override
    public void delete(OrderId orderId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement deleteItems = connection.prepareStatement(
                "DELETE FROM order_items WHERE order_id = ?");
            deleteItems.setString(1, orderId.value().toString());
            deleteItems.executeUpdate();

            PreparedStatement deleteOrder = connection.prepareStatement(
                "DELETE FROM orders WHERE id = ?");
            deleteOrder.setString(1, orderId.value().toString());
            deleteOrder.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement deleteItems = connection.prepareStatement(
                "DELETE FROM order_items");
            deleteItems.executeUpdate();

            PreparedStatement deleteOrder = connection.prepareStatement(
                "DELETE FROM orders");
            deleteOrder.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void persistOrderItems(Order order){
        try {
            for (OrderItem item : order.getOrderItemList()) {
                Connection connection = ConnectionScoped.CONNECTION.get();
                try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE product_id = ?")) {
                    ps.setString(1, order.getOrderId().value().toString());
                    ps.setString(2, item.productId().value().toString());
                    ps.setInt(3, item.quantity());
                    ps.setDouble(4, item.price());
                    ps.setString(5, item.productId().value().toString());
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void persistOrder(Order order) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO orders (customer_id, state) VALUES (?, ?) ON DUPLICATE KEY UPDATE customer_id = ?");
            ps.setString(2, order.getCustomerId().id().toString());
            ps.setString(3, convertToString(order.getState()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToString(OrderEvent state) {
        return switch (state) {
            case OrderCreated _ -> "CREATED";
            case OrderCanceled _ -> "CANCELED";
            case OrderSubmitted _ -> "SUBMITTED";
            case OrderItemAdded _ -> "ITEM_ADDED";
        };
    }
}
