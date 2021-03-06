package continuum.rest.http;

import continuum.Continuum;
import continuum.atom.Atom;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.REST;
import continuum.rest.http.exception.BadRequestException;
import continuum.rest.http.exception.MethodNotAllowedException;

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
                        .fields(request.fields)
                        .values(Continuum.values(request.min, request.max, request.count, request.sum, request.value))
                        .timestamp(request.timestamp)
                        .build();
        continuum.write(atom);
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

    @SuppressWarnings("unchecked")
    public class WriteRequest {
        public String name;
        public Double min = null;
        public Double max = null;
        public Double count = null;
        public Double sum = null;
        public Double value = null;
        long timestamp;
        Particles particles;
        Fields fields;

        public WriteRequest(Map<String, Object> data) throws Exception {
            particles = Continuum.particles();
            fields = Continuum.fields();
            timestamp = System.currentTimeMillis();
            for (String key : data.keySet()) {

                Object value = data.get(key);

                if (value == null) continue;

                switch (key) {
                    case "name":
                        name = (String) value;
                        break;
                    case "min":
                        this.min = (double)value;
                        break;
                    case "max":
                        this.max = (double)value;
                        break;
                    case "count":
                        this.count = (double)value;
                        break;
                    case "sum":
                        this.sum = (double)value;
                        break;
                    case "value":
                        if (value instanceof String)
                            this.value = Double.valueOf((String)value);
                        else
                            this.value = (double)value;
                        break;
                    case "values":
                        Map<String, Double> vs = (Map<String, Double>)value;
                        if (this.min == null) this.min = vs.get("min");
                        if (this.max == null) this.max = vs.get("max");
                        if (this.count == null) this.count = vs.get("count");
                        if (this.sum == null) this.sum = vs.get("sum");
                        if (this.value == null) this.value = vs.get("value");
                        break;
                    case "timestamp":
                        timestamp = (long)value;
                        break;
                    case "fields":
                        fields.putAll((Map)value);
                        break;
                    default:
                        particles.put(key, (String)value);
                        break;
                }
            }
            check("name", name);
            check("value", value);

            if (min == null) min = value;
            if (max == null) max = value;
            if (count == null) count = 1.0d;
            if (sum == null) sum = value;
        }

        private void check(String name, Object value) throws Exception {
            if (value == null) {
                throw new BadRequestException("Parameter: " + name + " is required");
            }
        }
    }
}
