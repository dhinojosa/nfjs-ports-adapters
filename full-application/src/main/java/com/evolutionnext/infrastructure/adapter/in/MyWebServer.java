package com.evolutionnext.infrastructure.adapter.in;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MyWebServer {
    private final OrderHandler orderHandler;

    public MyWebServer(OrderHandler orderHandler) throws IOException {
        this.orderHandler = orderHandler;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/order", orderHandler);
        server.start();
        System.out.println("Server started on port 8080");
    }
}
