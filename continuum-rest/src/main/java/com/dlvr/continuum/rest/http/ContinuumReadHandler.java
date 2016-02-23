package com.dlvr.continuum.rest.http;

import com.dlvr.continuum.rest.http.exception.MethodNotAllowedException;

import java.util.Map;

/**
 * Continuum read endpoint
 * Created by zack on 2/23/16.
 */
public class ContinuumReadHandler implements HttpRequestHandler {
    @Override
    public String getPath() {
        return "/api/1.0/read";
    }

    @Override
    public Object onGet(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public Object onPost(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onPut(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onDelete() throws Exception {
        throw new MethodNotAllowedException("");
    }
}
