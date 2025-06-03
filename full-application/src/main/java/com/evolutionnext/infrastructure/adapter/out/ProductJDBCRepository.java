package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.application.port.out.ProductRepository;
import com.evolutionnext.domain.aggregates.product.Product;
import com.evolutionnext.domain.aggregates.product.ProductId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductJDBCRepository implements ProductRepository {

    @Override
    public Optional<Product> load(ProductId productId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM products WHERE id = ?");
            preparedStatement.setObject(1, productId.value());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Product(
                    new ProductId(resultSet.getObject("id", UUID.class)),
                    resultSet.getString("name"),
                    resultSet.getBigDecimal("price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Product product){
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement preparedStatement = connection
                .prepareStatement("""
                    INSERT INTO products (id, name, price)
                        VALUES (?, ?, ?)
                        ON CONFLICT (id) DO UPDATE
                        SET name = EXCLUDED.name,
                            price = EXCLUDED.price""");
            preparedStatement.setObject(1, product.id().value());
            preparedStatement.setString(2, product.name());
            preparedStatement.setBigDecimal(3, product.price());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM products");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(new Product(
                    new ProductId(resultSet.getObject("id", UUID.class)),
                    resultSet.getString("name"),
                    resultSet.getBigDecimal("price")
                ));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(ProductId productId) {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM products WHERE id = ?");
            preparedStatement.setObject(1, productId.value());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            Connection connection = ConnectionScoped.CONNECTION.get();
            PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM products");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
