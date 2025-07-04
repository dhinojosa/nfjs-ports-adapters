package com.evolutionnext.infrastructure.adapter.out;


import com.evolutionnext.application.port.out.CustomerRepository;
import com.evolutionnext.domain.aggregates.customer.Customer;
import com.evolutionnext.domain.aggregates.customer.CustomerId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomerJDBCRepository implements CustomerRepository {

    @Override
    public Optional<Customer> load(CustomerId customerId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            String sql = "SELECT * FROM customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, customerId.id());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Customer(
                    new CustomerId(resultSet.getObject("id", UUID.class)),
                    resultSet.getString("name"),
                    resultSet.getBigDecimal("credit_limit")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Customer customer) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            String sql = """
                INSERT INTO customers (id, name, credit_limit)
                VALUES (?, ?, ?);""";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, customer.id().id());
            preparedStatement.setString(2, customer.name());
            preparedStatement.setBigDecimal(3, customer.creditLimit());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("preview")
    @Override
    public List<Customer> findAll() {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            String sql = "SELECT * FROM customers";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(new Customer(
                    new CustomerId(UUID.fromString(resultSet.getString("id"))),
                    resultSet.getString("name"),
                    resultSet.getBigDecimal("credit_limit")
                ));
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(CustomerId customerId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            String sql = "DELETE FROM customers WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, customerId.id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SqlWithoutWhere")
    @Override
    public void deleteAll() {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            String sql = "DELETE FROM customers";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
