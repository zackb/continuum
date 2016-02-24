package com.dlvr.continuum.rest.http.netty;

import com.dlvr.continuum.rest.http.HttpRequestHandler;
import com.dlvr.continuum.rest.http.HttpServerConfig;
import com.dlvr.continuum.rest.http.exception.HttpException;
import com.dlvr.continuum.rest.http.exception.InternalServerErrorException;
import com.dlvr.continuum.rest.http.exception.MethodNotAllowedException;
import com.dlvr.continuum.rest.http.exception.NotFoundException;
import com.dlvr.continuum.util.JSON;
import com.dlvr.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.*;
import java.util.Map.Entry;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;


public class NettyHttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest request;

    private Optional<HttpRequestHandler> handler = Optional.empty();

    private final Map<String, Object> parameters = new HashMap<>();

    private final Set<HttpRequestHandler> handlers;

    private final StringBuilder buf = new StringBuilder();

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        JSON.configureMapper(mapper);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }


    public NettyHttpServerHandler(HttpServerConfig config) {
        this.handlers = config.handlers;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;

            buf.setLength(0);

            String uri = request.uri();
            if (uri.indexOf('?') > 0) {
                uri = uri.substring(0, uri.indexOf('?'));
            }
            handler = selectHandler(uri);

            parseHeaders();
            parseParameters();
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;

            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                buf.append(content.toString(CharsetUtil.UTF_8));
            }

            if (msg instanceof LastHttpContent) {
                LastHttpContent trailer = (LastHttpContent) msg;
                if (!writeResponse(trailer, ctx)) {
                    // If keep-alive is off, close the connection once the content is fully written.
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    private Optional<HttpRequestHandler> selectHandler(String path) {
        String trimmedPath = StringUtil.rtrim(path, "/");
        return handlers.stream().filter(handler -> handler.getPath().equals(trimmedPath)).findFirst();
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {

        boolean keepAlive = HttpUtil.isKeepAlive(request);

        String data = buf.toString();
        if (!StringUtil.empty(data)) {
            Map<String, Object> json = JSON.decode(data);
            parameters.putAll(json);
        }

        HttpResponseStatus status = currentObj.decoderResult().isSuccess() ? OK : BAD_REQUEST;

        String responseString = "";
        Object result;
        try {
            result = callHandler();
        } catch (HttpException e) {
            System.err.println("Failed handling request: " + request.uri() + " : " + e.getMessage());
            e.printStackTrace();
            status = HttpResponseStatus.valueOf(e.getStatusCode());
            result = createErrorResult(e);
        }
        try {
            responseString = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            System.err.println("Failed encoding json: " + e.getMessage());
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer(responseString, CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "application/json");


        if (keepAlive) {
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        ctx.write(response);

        return keepAlive;
    }

    private Object callHandler() throws HttpException {
        Object result = null;
        if (!handler.isPresent()) {
            throw new NotFoundException(request.uri());
        }

        try {
            switch (request.method().name()) {
                case "GET":
                    result = handler.get().onGet(parameters);
                    break;
                case "POST":
                    result = handler.get().onPost(parameters);
                    break;
                case "PUT":
                    result = handler.get().onPut(parameters);
                    break;
                case "DELETE":
                    result = handler.get().onDelete();
                    break;
                default:
                    System.err.println("No handler supports: " + request.method().name() + " " + request.uri());
                    throw new MethodNotAllowedException("The resource " + request.uri() +
                            " is not capable of handling: " + request.method());

            }
        } catch (Exception e) {
            if (e instanceof HttpException) {
                throw (HttpException)e;
            }
            throw new InternalServerErrorException(e.getMessage(), e);
        }
        return result;
    }

    private Map<String, String> createErrorResult(HttpException e) {
        String reason = e.getReasonMessage();
        String message = e.getMessage();
        if (StringUtil.empty(message) && e.getCause() != null) {
            message = e.getCause().getMessage();
        }
        Map<String, String> result = new HashMap<>();
        result.put("message", reason);
        result.put("cause", message);
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void parseParameters() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            for (Entry<String, List<String>> p : params.entrySet()) {
                String key = p.getKey();
                List<String> vals = p.getValue();
                for (String val : vals) {
                    parameters.put(key, val);
                }
            }
        }
    }

    private void parseHeaders() {
        HttpHeaders headers = request.headers();
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> h : headers) {
                String key = h.getKey().toLowerCase();
                if (key.startsWith("x-dlvr")) {
                    String value = h.getValue();
                    parameters.put(key.substring(7), value);
                }
            }
        }
    }
}

