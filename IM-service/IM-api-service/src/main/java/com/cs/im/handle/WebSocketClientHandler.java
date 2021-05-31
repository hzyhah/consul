package com.cs.im.handle;


import com.alibaba.fastjson.JSON;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description WebSocketClientHandler
 * @Author honry.guan
 * @Date 2020/5/25 10:47
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * 负责和服务器握手
     */
    private final WebSocketClientHandshaker handshaker;
    /**
     * 握手结果
     */
    private ChannelPromise handshakeFuture;

    @Autowired
    Jedis jedis;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client 已激活");
        handshaker.handshake(ctx.channel());
        jedis.set("123",new String("123"));

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开");
    }

    /**
     * 当前handler被添加到pipeline时，new出握手的结果示例，以备将来使用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，处理参数 by zhengkai.blog.csdn.net
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            Map paramMap=getUrlParams(uri);
            System.out.println("接收到的参数是："+ JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }

        }else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
        }
        super.channelRead(ctx,msg);
    }
    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        //握手未完成，完成握手
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("完成连接");
                handshakeFuture.setSuccess();

            } catch (WebSocketHandshakeException e) {
                System.out.println("握手连接失败");
                handshakeFuture.setFailure(e);
            }
            return;
        }
        //握手完成，升级为websocket，不应该再收到http报文
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        System.out.println("!!!!!!!!!!!!!!!!!!");

        //处理websocket报文
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) frame;
            System.out.println("收到消息：" + textWebSocketFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("客户端收到pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("客户端手动关闭");
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }
}
