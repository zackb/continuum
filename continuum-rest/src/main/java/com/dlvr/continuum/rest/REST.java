package com.dlvr.continuum.rest;

import com.dlvr.continuum.rest.http.HttpServerConfig;
import com.dlvr.continuum.rest.http.IHttpServer;
import com.dlvr.continuum.rest.http.netty.NettyHttpServer;

/**
 * Main library entry point
 * Created by zack on 2/23/16.
 */
public class REST {

    private IHttpServer httpServer;

    public REST(int port) {
        HttpServerConfig config = HttpServerConfig.basicConfig();
        config.port = port;
        this.httpServer = new NettyHttpServer(config);
    }

    public REST start() {
        httpServer.start();
        return this;
    }

    public void stop() {
        httpServer.stop();
    }

    public static void main(String[] args) throws Exception {
        REST rest = new REST(1337).start();
        byte[] b = new byte[1];
        for (; b[0] != 'q'; System.in.read(b));
        rest.stop();
    }
}
