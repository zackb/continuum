package continuum.rest.http;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration for HTTP servers
 */
public class HttpServerConfig {

    // TCP port to listen on
    public int port;

    // Number of workers to process connections
    public int workerCount;

    public Set<HttpRequestHandler> handlers = new HashSet<>();

    public static HttpServerConfig basicConfig() {
        HttpServerConfig config = new HttpServerConfig();
        config.port = 8081;
        config.workerCount = 150;
        config.handlers.add(new VitalsHandler());
        config.handlers.add(new VitalsMetricsHandler());
        return config;
    }
}
