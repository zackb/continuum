package continuum.rest.http;

import continuum.util.Metrics;

import java.util.Map;

/**
 * Expose Metrics via http
 * Created by zack on 2/29/16.
 */
public class VitalsMetricsHandler implements HttpRequestHandler {

    @Override
    public String getPath() {
        return "/vitals/metrics";
    }

    @Override
    public Object onGet(Map<String, Object> params) {
        return Metrics.report();
    }

    @Override
    public Object onPost(Map<String, Object> params) {
        return null;
    }

    @Override
    public Object onPut(Map<String, Object> params) {
        return null;
    }

    @Override
    public Object onDelete() {
        return null;
    }
}
