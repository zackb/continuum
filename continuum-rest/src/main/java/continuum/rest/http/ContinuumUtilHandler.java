package continuum.rest.http;

import continuum.REST;
import continuum.slice.Const;
import continuum.util.Strings;
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
        String name = (String)params.get("name");
        if (Strings.empty(name)) {
            name = Const.SWILDCARD;
        }

        Map<String, String> result = new HashMap<>(1);

        if (cmd.equals("trim")) {
            REST.instance().continuum().delete(name, Interval.valueOf("5d"));
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
