package continuum.rest.http;

import continuum.Continuum;
import continuum.atom.Fields;
import continuum.atom.Particles;
import continuum.rest.http.exception.BadRequestException;
import continuum.slice.Function;
import continuum.util.datetime.Interval;

import java.util.Map;

/**
 * Wraps key/value parameters to corresponding {@link continuum.slice.Scan} variables
 */
public class ReadRequest {
    public String name;
    public long start = System.currentTimeMillis();
    public long end = System.currentTimeMillis() - Interval.valueOf("1h").millis();
    public Interval interval;
    public Function function;
    public Particles particles;
    public Fields fields;
    public String[] group;
    public Integer limit;

    @SuppressWarnings("unchecked")
    public ReadRequest(Map<String, Object> data) throws Exception {

        particles = Continuum.particles();
        fields = Continuum.fields();

        for (String key : data.keySet()) {
            Object value = data.get(key);
            switch (key) {
                case "name":
                    name = (String) value;
                    break;
                case "start":
                    start = Interval.valueOf((String) value).epoch();
                    break;
                case "end":
                    end = Interval.valueOf((String) value).epoch();
                    break;
                case "interval":
                    interval = Interval.valueOf((String) value);
                    break;
                case "fn":
                    function = Function.fromString((String) value);
                    break;
                case "group":
                    group = ((String) value).split(",");
                    break;
                case "limit":
                    limit = (int) value;
                case "fields":
                    fields.putAll((Map<String, Object>) value);
                    break;
                case "timestamp":
                case "value":
                case "values":
                    break;
                case "particles":
                default:
                    particles.put(key, (String) value);
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
