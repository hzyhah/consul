package com.cs.im.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

public class WebSocketServerHandler  extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;
    /**
     * 接收客户端发过来的消息并处理
     * FullHttpRequest ：
     *  官网解释：Combine the {@link HttpRequest} and {@link FullHttpMessage}, so the request is a <i>complete</i> HTTP
     * request.
     * 这个请求是 代表http请求完成的标记。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if(msg instanceof FullHttpRequest){//接收到客户端的握手请求，开始处理握手
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame){//接收到客户端发过来的消息（只过滤文本消息），处理后发给客户端。
            handleWebSocketFrame(ctx, (WebSocketFrame)msg);
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req) throws Exception{
        /**
         * 如果不成功或者消息头不包含"Upgrade"，说明不是websocket连接，报400异常。
         */
        if(!req.getDecoderResult().isSuccess()||(!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        /**
         * WebSocket是一种全新的协议，不属于http无状态协议，协议名为"ws"，这意味着一个websocket连接地址会是这样的写法：
         ws://127.0.0.1:8080/websocket。ws不是http，所以传统的web服务器不一定支持，需要服务器与浏览器同时支持， WebSocket才能正常运行，目前大部分浏览器都支持Websocket。
         WebSocketServerHandshaker 官网的解释是：服务器端Web套接字打开和关闭握手基类
         WebSocketServerHandshakerFactory 官网的解释是：自动检测正在使用的网络套接字协议的版本，并创建一个新的合适的 WebSocketServerHandshaker。
         */
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(req);//创建一个握手协议
        if(handshaker == null){
            /**
             * Return that we need cannot not support the web socket version
             * 返回不支持websocket 版本
             */
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), req);//开始握手
        }
    }
    /**
     * 我们判断数据类型，只支持文本类型
     * @param ctx
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame) {
        if(frame instanceof CloseWebSocketFrame){//如是接收到的是关闭websocket，就关闭连接
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if(frame instanceof PingWebSocketFrame){//如果信息是2进制数据，就反给它，
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if(!(frame instanceof TextWebSocketFrame)){//如果不是文本的数据，就报错。
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }

        String request = ((TextWebSocketFrame)frame).text();

        ctx.channel().write(new TextWebSocketFrame(request+",欢迎使用netty websocket 服务，现在时刻"+new Date().toString()));
    }
    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
        if(res.getStatus().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res,res.content().readableBytes());

        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);//发送消息
        if(!isKeepAlive(req)||res.getStatus().code()!= 200){//如果断开连接，或者发送不成功，断开连接。
            f.addListener(ChannelFutureListener.CLOSE);//关闭连接
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
