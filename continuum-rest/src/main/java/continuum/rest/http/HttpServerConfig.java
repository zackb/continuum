package continuum.rest.http;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Configuration for HTTP servers
 */
public class HttpServerConfig {

    private static final Logger log = Logger.getLogger(HttpServerConfig.class.getSimpleName());

    // TCP port to listen on
    public int port;

    // Number of workers to process connections
    public int workerCount;

    private Set<HttpRequestHandler> handlers = new HashSet<>();

    public static HttpServerConfig basicConfig() {
        HttpServerConfig config = new HttpServerConfig();
        config.port = 8081;
        config.workerCount = 150;
        config.addHandler(new VitalsHandler());
        config.addHandler(new VitalsMetricsHandler());
        return config;
    }

    public HttpServerConfig port(int port) {
        this.port = port;
        return this;
    }

    /**
     * Adds a HTTP endpoint handler. If there is already a handler registered
     * for the {@link HttpRequestHandler#getPath} it will be ignored
     * @param handler to add to the webserver
     */
    public void addHandler(HttpRequestHandler handler) {
        String path = handler.getPath();
        for (HttpRequestHandler h : handlers) {
            if (h.getPath().equals(path)) {
                log.warning("Not adding handler because a handler for this path already exists: " + path);
                return;
            }
        }
        handlers.add(handler);
    }

    public Set<HttpRequestHandler> getHandlers() {
        return handlers;
    }
}
