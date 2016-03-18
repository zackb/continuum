package continuum.rest.http;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Expose system vitals
 */
public class VitalsHandler implements HttpRequestHandler {
    @Override
    public String getPath() {
        return "/vitals/system";
    }

    @Override
    public Object onGet(Map<String, Object> params) {
        Runtime rt = Runtime.getRuntime();
        Map<String, Object> result = new HashMap<>();
        result.put("total_memory", rt.totalMemory());
        result.put("max_mem", rt.maxMemory());
        result.put("free_mem", rt.freeMemory());
        result.put("num_procs", rt.availableProcessors());
        result.put("thread_count", ManagementFactory.getThreadMXBean().getAllThreadIds().length);
        result.put("load_avg",  ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());

        return result;
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
