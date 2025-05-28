package com.evolutionnext.infrastructure.adapter.out;

import com.evolutionnext.application.port.out.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class JDBCTransactional implements Transactional {
    private final DataSource dataSource;

    public JDBCTransactional(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T transactionally(Supplier<T> work) {
        try {
            return ScopedValue.where(ConnectionScoped.CONNECTION, dataSource.getConnection()).call(new ScopedValue.CallableOp<>() {
                @Override
                public T call() {
                    try (Connection connection = ConnectionScoped.CONNECTION.get()) {
                        connection.setAutoCommit(false); // Begin transaction
                        T result = work.get();           // Execute work
                        connection.commit();             // Commit transaction
                        return result;
                    } catch (Exception e) {
                        try {
                            ConnectionScoped.CONNECTION.get().rollback();          // Rollback transaction if there's an error
                        } catch (SQLException ex) {
                            throw new RuntimeException("Transaction rollback failed.", ex);
                        }
                        throw new RuntimeException("Transaction rolled back due to an exception.", e);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
