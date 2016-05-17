package continuum.rest.http;

import continuum.Continuum;
import continuum.REST;
import continuum.util.datetime.Interval;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility HTTP endpoints for development
 */
public class ContinuumUtilHandler implements HttpRequestHandler {
    @Override
    public String getPath() {
        return "/util";
    }

    @Override
    public Object onGet(Map<String, Object> params) throws Exception {

        String cmd = (String)params.get("cmd");

        Map<String, String> result = new HashMap<>(1);

        if (cmd.equals("trim")) {
            REST.instance().continuum().delete(Interval.valueOf("5d"));
        }

        result.put("message", "OK");

        return result;
    }

    @Override
    public Object onPost(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public Object onPut(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public Object onDelete() throws Exception {
        return null;
    }
}
