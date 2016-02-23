package com.dlvr.continuum.rest.http;

import java.util.Map;

/**
 * HTTP request handler interface
 */
public interface HttpRequestHandler {
    String getPath();
    Object onGet(Map<String, Object> params) throws Exception;
    Object onPost(Map<String, Object> params) throws Exception;
    Object onPut(Map<String, Object> params) throws Exception;
    Object onDelete() throws Exception;
}
