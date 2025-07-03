package com.evolutionnext.infrastructure.adapter.in;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyWebServer {
    private final OrderHandler orderHandler;
    private final HealthHandler healthHandler;

    public MyWebServer(OrderHandler orderHandler, HealthHandler healthHandler) throws IOException {
        this.orderHandler = orderHandler;
        this.healthHandler = healthHandler;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/order", orderHandler);
        server.createContext("/health", healthHandler);
        server.start();
        System.out.println("Server started on port 8080");
    }
}
