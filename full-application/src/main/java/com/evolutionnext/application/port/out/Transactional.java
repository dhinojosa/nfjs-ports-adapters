package com.evolutionnext.application.port.out;

import java.util.function.Supplier;

public interface Transactional {
    /**
     * Executes the given transactional logic within a transactional boundary.
     *
     * @param work The work to be executed within the transaction.
     * @param <T>  The type of the result of the transactional work.
     * @return The result of the transactional work.
     */
    <T> T transactionally(Supplier<T> work);
}
