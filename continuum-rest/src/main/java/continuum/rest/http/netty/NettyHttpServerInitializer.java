package continuum.rest.http.netty;

import continuum.rest.http.HttpServerConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Initializes a netty server
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final HttpServerConfig config;

    public NettyHttpServerInitializer(HttpServerConfig config) {
        this.config = config;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        //p.addLast(new HttpServerCodec());
        p.addLast(new NettyHttpServerHandler(config));
    }
}
