package com.evolutionnext.infrastructure.adapter.out;

import java.lang.ScopedValue;
import java.sql.Connection;

public class ConnectionScoped {
    public static final ScopedValue<Connection> CONNECTION =
        ScopedValue.newInstance();
}
