package com.dlvr.continuum.rest.http;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.atom.Particles;
import com.dlvr.continuum.db.slice.Function;
import com.dlvr.continuum.db.slice.Slice;
import com.dlvr.continuum.db.slice.SliceResult;
import com.dlvr.continuum.rest.REST;
import com.dlvr.continuum.rest.http.exception.MethodNotAllowedException;
import com.dlvr.continuum.util.datetime.Interval;

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
        ReadRequest request = new ReadRequest(params);
        Slice slice = Continuum.slice(request.name)
                .start(request.start)
                .end(request.end)
                .interval(request.interval)
                .function(request.function)
                .particles(request.particles)
                .build();

        SliceResult result = REST.instance()
                .continuum()
                .db()
                .slice(slice);
        return result;
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

    class ReadRequest {
        public String name;
        public Interval start = Interval.valueOf("0s");
        public Interval end = Interval.valueOf("1h");
        Interval interval;
        Function function;
        Particles particles;

        public ReadRequest(Map<String, Object> data) throws Exception {

            particles = Continuum.particles();

            for (String key : data.keySet()) {
                Object value = data.get(key);
                switch (key) {
                    case "name":
                        name = (String)value;
                        break;
                    case "start":
                        start = Interval.valueOf((String)value);
                        break;
                    case "end":
                        end = Interval.valueOf((String)value);
                        break;
                    case "interval":
                        interval = Interval.valueOf((String)value);
                        break;
                    case "fn":
                        function = Function.fromString((String)value);
                        break;
                    default:
                        particles.put(key, (String)value);
                        break;
                }
            }
            check("name", name);
            check("start", start);
            check("end", end);
        }

        private void check(String name, Object value) throws Exception {
            if (value == null) {
                throw new Exception("Parameter: " + name + " is required");
            }
        }
    }
}
