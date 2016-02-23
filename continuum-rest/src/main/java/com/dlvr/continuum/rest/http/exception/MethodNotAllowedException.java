package com.dlvr.continuum.rest.http.exception;

/**
 * 405 Method Not Allowed
 */
public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 405;
    }

    @Override
    public String getReasonMessage() {
        return "Method Not Allowed";
    }
}
