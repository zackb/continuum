package com.dlvr.continuum.rest;

import com.dlvr.continuum.Continuum;
import com.dlvr.continuum.rest.client.Client;
import com.dlvr.continuum.rest.http.ContinuumReadHandler;
import com.dlvr.continuum.rest.http.ContinuumWriteHandler;
import com.dlvr.continuum.rest.http.HttpServerConfig;
import com.dlvr.continuum.rest.http.IHttpServer;
import com.dlvr.continuum.rest.http.netty.NettyHttpServer;
import com.dlvr.continuum.universe.Universe;

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

    public REST(Continuum continuum, int port) throws Exception {
        if (instance != null) {
            throw new Exception("Already Running!");
        }
        this.continuum = continuum;
        HttpServerConfig config = HttpServerConfig.basicConfig();
        config.port = port;
        config.handlers.add(new ContinuumReadHandler());
        config.handlers.add(new ContinuumWriteHandler());
        this.httpServer = new NettyHttpServer(config);
        instance = this; // AHHHH!
    }

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
