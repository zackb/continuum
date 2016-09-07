package continuum;

import continuum.rest.client.Client;
import continuum.rest.http.*;
import continuum.rest.http.netty.NettyHttpServer;
import continuum.universe.Universe;

/**
 * Main library entry point
 * Created by zack on 2/23/16.
 */
public class REST {

    private static REST instance;

    private final IHttpServer httpServer;

    private final Continuum continuum;

    public REST(Continuum continuum) throws Exception {
        this(continuum, 1337);
    }


    /**
     * Create a new REST wrapper around the data store
     * @param continuum data store to expose REST endpoints for
     * @param port to bind the http server to
     * @throws Exception if the webserver is already running
     */
    public REST(Continuum continuum, int port) throws Exception {
        this(continuum,
             HttpServerConfig
                     .basicConfig()
                        .port(port));
    }

    /**
     * Create a new REST wrapper around the data store
     * @param continuum data store to expose REST endpoints for
     * @param config http server configuration values and custome endpoint handlers
     * @throws Exception if the webserver is already running
     */
    public REST(Continuum continuum, HttpServerConfig config) throws Exception {
        if (instance != null) {
            throw new Exception("Already Running!");
        }
        this.continuum = continuum;
        config.addHandler(new ContinuumReadHandler());
        config.addHandler(new ContinuumWriteHandler());
        config.addHandler(new ContinuumUtilHandler());
        config.addHandler(new ContinuumCountHandler());
        this.httpServer = new NettyHttpServer(config);
        instance = this; // AHHHH!
    }

    /**
     * Starts the http server. Bind and listen on the configured port
     * @return a reference to this object
     */
    public REST start() {
        httpServer.start();
        return this;
    }

    public void stop() {
        httpServer.stop();
    }

    public static REST instance() {
        return instance;
    }

    public Continuum continuum() {
        return continuum;
    }

    /**
     * Create a REST client for a given Continuum rest server
     * @param host rest host
     * @param port rest port
     * @return new REST client
     */
    public static Client client(String host, int port) {
        return new Client(host, port);
    }

    public static void main(String[] args) throws Exception {
        Universe universe = Universe.bigBang("/Users/zack/code/dlvr/continuum/continuum-core/universe.meta");
        Continuum continuum = Continuum.continuum()
                .name(universe.name())
                .base(universe.hot())
                .dimension(universe.dimension())
                .open();

        REST rest = new REST(continuum, 1337).start();

        byte[] b = new byte[1];
        for (; b[0] != 'q'; System.in.read(b));
        rest.stop();
    }

}
