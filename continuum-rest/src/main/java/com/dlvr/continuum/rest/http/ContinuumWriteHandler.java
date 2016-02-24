package com.dlvr.continuum.rest.http;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Atom;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.rest.REST;
import com.dlvr.continuum.rest.http.exception.MethodNotAllowedException;

import java.util.Map;

/**
 * Continuum write endpoint
 * Created by zack on 2/23/16.
 */
public class ContinuumWriteHandler implements HttpRequestHandler {

    @Override
    public String getPath() {
        return "/api/1.0/write";
    }

    @Override
    public Object onGet(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onPost(Map<String, Object> params) throws Exception {
        WriteRequest request = new WriteRequest(params);
        Continuum continuum = REST.instance().continuum();
        Atom atom = continuum.atom()
                        .name(request.name)
                        .particles(request.particles)
                        .value(request.value)
                        .timestamp(request.timestamp)
                        .build();
        continuum.db().write(atom);
        return null;
    }

    @Override
    public Object onPut(Map<String, Object> params) throws Exception {
        throw new MethodNotAllowedException("");
    }

    @Override
    public Object onDelete() throws Exception {
        throw new MethodNotAllowedException("");
    }

    public class WriteRequest {
        public String name;
        public double value;
        long timestamp;
        Particles particles;

        public WriteRequest(Map<String, Object> data) throws Exception {
            particles = Continuum.particles();
            timestamp = System.currentTimeMillis();
            for (String key : data.keySet()) {
                Object value = data.get(key);
                switch (key) {
                    case "name":
                        name = (String) value;
                        break;
                    case "value":
                        this.value = Double.parseDouble((String)value);
                        break;
                    case "timestamp":
                        timestamp = Long.parseLong((String)value);
                        break;
                    default:
                        particles.put(key, (String)value);
                        break;
                }
            }
            check("name", name);
            check("value", this.value);
        }

        private void check(String name, Object value) throws Exception {
            if (value == null) {
                throw new Exception("Parameter: " + name + " is required");
            }
        }
    }
}
