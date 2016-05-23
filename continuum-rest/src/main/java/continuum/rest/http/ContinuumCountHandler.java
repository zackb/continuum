package continuum.rest.http;

import continuum.REST;
import continuum.rest.http.exception.MethodNotAllowedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Access point to {@link continuum.Continuum#count()}
 */
public class ContinuumCountHandler implements HttpRequestHandler {
    @Override
    public String getPath() {
        return "/api/1.0/count";
    }

    @Override
    public Object onGet(Map<String, Object> params) throws Exception {
        Map<String, Long> result = new HashMap<>(1);
        result.put("count", REST.instance().continuum().count());
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
}
