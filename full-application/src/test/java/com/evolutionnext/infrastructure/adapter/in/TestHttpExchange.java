package com.evolutionnext.infrastructure.adapter.in;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

public class TestHttpExchange extends HttpExchange {

    private final String method;
    private final ByteArrayInputStream requestBody;
    private final ByteArrayOutputStream responseStream;

    public TestHttpExchange(String method, String requestBody, ByteArrayOutputStream responseStream) {
        this.method = method;
        this.requestBody = new ByteArrayInputStream(requestBody.getBytes());
        this.responseStream = responseStream;
    }

    @Override
    public Headers getRequestHeaders() {
        return new Headers();
    }

    @Override
    public Headers getResponseHeaders() {
        return new Headers();
    }

    @Override
    public URI getRequestURI() {
        return null;
    }

    @Override
    public String getRequestMethod() {
        return method;
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return requestBody;
    }

    @Override
    public OutputStream getResponseBody() {
        return responseStream;
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {

    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "";
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }
}
