package com.dlvr.continuum.rest.http.exception;


/**
 * HTTP Exception
 */
public abstract class HttpException extends Exception {
    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public HttpException(String message) {
        super(message);
    }
    public abstract int getStatusCode();
    public abstract String getReasonMessage();
}
