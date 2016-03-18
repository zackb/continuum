package continuum.rest.http;

import continuum.Continuum;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.REST;
import continuum.rest.http.exception.BadRequestException;
import continuum.rest.http.exception.MethodNotAllowedException;
import continuum.slice.Function;
import continuum.slice.Scan;
import continuum.util.datetime.Interval;

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
        Scan scan = REST.instance().continuum()
                .scan(request.name)
                .start(request.start)
                .end(request.end)
                .interval(request.interval)
                .function(request.function)
                .particles(request.particles)
                .fields(request.fields)
                .group(request.group)
                .build();

        return REST.instance()
                .continuum()
                    .slice(scan);
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
        public long start = System.currentTimeMillis();
        public long end = System.currentTimeMillis() - Interval.valueOf("1h").millis();
        Interval interval;
        Function function;
        Particles particles;
        Fields fields;
        String[] group;

        @SuppressWarnings("unchecked")
        public ReadRequest(Map<String, Object> data) throws Exception {

            particles = Continuum.particles();
            fields = Continuum.fields();

            for (String key : data.keySet()) {
                Object value = data.get(key);
                switch (key) {
                    case "name":
                        name = (String)value;
                        break;
                    case "start":
                        start = Interval.valueOf((String)value).epoch();
                        break;
                    case "end":
                        end = Interval.valueOf((String)value).epoch();
                        break;
                    case "interval":
                        interval = Interval.valueOf((String)value);
                        break;
                    case "fn":
                        function = Function.fromString((String)value);
                        break;
                    case "group":
                        group = ((String)value).split(",");
                        break;
                    case "fields":
                        fields.putAll((Map<String, Object>)value);
                        break;
                    case "timestamp":
                    case "value":
                    case "values":
                        break;
                    case "particles":
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
                throw new BadRequestException("Parameter: " + name + " is required");
            }
        }
    }
}
