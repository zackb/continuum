package com.dlvr.continuum.rest.http;

import com.dlvr.continuum.util.Bytes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * HTTP request
 * Created by zack on 3/14/16.
 */
public class Request {
    String url;
    String method;
    Map<String, String> headers;
    Object data = "";
    int readTimeout = 1000 * 60 * 20; // 20 seconds
    int connectTimeout = 1000 * 60 * 10; // 10 seconds

    public Request url(String url) {
        this.url = url;
        return this;
    }
    public Request method(String method) {
        this.method = method;
        return this;
    }
    public Request headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
    public Request data(Object data) {
        this.data = data;
        return this;
    }
    public Request readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
    public Request connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(Bytes.bytes(data.toString()));
    }
}
