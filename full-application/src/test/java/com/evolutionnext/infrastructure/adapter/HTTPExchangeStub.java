package com.evolutionnext.infrastructure.adapter;

import java.util.HashMap;
import java.util.Map;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

public class HTTPExchangeStub extends HttpExchange {

    private final Headers requestHeaders;
    private final Headers responseHeaders;
    private final URI requestURI;
    private final String requestMethod;
    private final HttpContext httpContext;
    private InputStream requestBody;
    private OutputStream responseBody;
    private final InetSocketAddress remoteAddress;
    private final InetSocketAddress localAddress;
    private final String protocol;
    private final HttpPrincipal principal;
    private int responseCode;
    private final Map<String, Object> attributes = new HashMap<>();

    private HTTPExchangeStub(Headers requestHeaders,
                             Headers responseHeaders,
                             URI requestURI,
                             String requestMethod,
                             HttpContext httpContext,
                             InputStream requestBody,
                             OutputStream responseBody,
                             InetSocketAddress remoteAddress,
                             InetSocketAddress localAddress,
                             String protocol,
                             HttpPrincipal principal) {
        this.requestHeaders = requestHeaders;
        this.responseHeaders = responseHeaders;
        this.requestURI = requestURI;
        this.requestMethod = requestMethod;
        this.httpContext = httpContext;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
        this.protocol = protocol;
        this.principal = principal;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Headers getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public Headers getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public URI getRequestURI() {
        return requestURI;
    }

    @Override
    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public HttpContext getHttpContext() {
        return httpContext;
    }

    @Override
    public void close() {
        try {
            if (requestBody != null) requestBody.close();
            if (responseBody != null) responseBody.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getRequestBody() {
        return requestBody;
    }

    @Override
    public OutputStream getResponseBody() {
        return responseBody;
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
        this.responseCode = rCode;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }


    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {
        if (i != null && o != null) {
            this.requestBody = i;
            this.responseBody = o;
        }
    }

    @Override
    public HttpPrincipal getPrincipal() {
        return principal;
    }

    public static class Builder {
        private Headers requestHeaders;
        private Headers responseHeaders;
        private URI requestURI;
        private String requestMethod;
        private HttpContext httpContext;
        private InputStream requestBody;
        private OutputStream responseBody;
        private InetSocketAddress remoteAddress;
        private InetSocketAddress localAddress;
        private String protocol;
        private HttpPrincipal principal;

        public Builder setRequestHeaders(Headers requestHeaders) {
            this.requestHeaders = requestHeaders;
            return this;
        }

        public Builder setResponseHeaders(Headers responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        public Builder setRequestURI(URI requestURI) {
            this.requestURI = requestURI;
            return this;
        }

        public Builder setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder setHttpContext(HttpContext httpContext) {
            this.httpContext = httpContext;
            return this;
        }

        public Builder setRequestBody(InputStream requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder setResponseBody(OutputStream responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public Builder setRemoteAddress(InetSocketAddress remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        public Builder setLocalAddress(InetSocketAddress localAddress) {
            this.localAddress = localAddress;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setPrincipal(HttpPrincipal principal) {
            this.principal = principal;
            return this;
        }

        public HTTPExchangeStub build() {
            return new HTTPExchangeStub(requestHeaders, responseHeaders,
                requestURI, requestMethod, httpContext, requestBody,
                responseBody, remoteAddress, localAddress, protocol, principal);
        }
    }
}
