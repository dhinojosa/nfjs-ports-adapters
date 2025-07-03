package com.evolutionnext.infrastructure.adapter.in;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Home Page";
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, response.length());
        try (var outputStream = exchange.getResponseBody()) {
            outputStream.write(response.getBytes());
        }
    }
}
