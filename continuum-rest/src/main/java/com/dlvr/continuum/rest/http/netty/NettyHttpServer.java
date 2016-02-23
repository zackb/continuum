package com.dlvr.continuum.rest.http.netty;

import com.dlvr.continuum.rest.http.HttpServerConfig;
import com.dlvr.continuum.rest.http.IHttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * HttpServer implemented with netty/nio
 */
public class NettyHttpServer implements IHttpServer {

    private final HttpServerConfig config;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;


    public NettyHttpServer(HttpServerConfig config) {
        this.config = config;
    }

    public void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_BACKLOG, 12048);
        b.option(ChannelOption.SO_REUSEADDR, true);
        //b.option(ChannelOption.SO_KEEPALIVE, true);
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                //.handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new NettyHttpServerInitializer(config));

        try {
            channel = b.bind(config.port).sync().channel();
        } catch (InterruptedException e) {
            System.out.println("Interrupted startup: " + e.getMessage());
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        if (channel != null && channel.isOpen()) {
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                System.out.println("Failed disconnecting server: " + e.getMessage());
            }
        }
    }
}
