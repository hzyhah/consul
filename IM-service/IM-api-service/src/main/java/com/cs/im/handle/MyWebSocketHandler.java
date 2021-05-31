package com.cs.im.handle;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！");

    }

    //    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //首次连接是FullHttpRequest，处理参数 by zhengkai.blog.csdn.net
//        if (null != msg && msg instanceof FullHttpRequest) {
//            FullHttpRequest request = (FullHttpRequest) msg;
//            String uri = request.uri();
//
//            Map paramMap=getUrlParams(uri);
//            System.out.println("接收到的参数是："+paramMap);
//            //如果url包含参数，需要处理
//            if(uri.contains("?")){
//                String newUri=uri.substring(0,uri.indexOf("?"));
//                System.out.println(newUri);
//                request.setUri(newUri);
//            }
//
//        }else if(msg instanceof TextWebSocketFrame){
//            //正常的TEXT消息类型
//            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
//            System.out.println("客户端收到服务器数据：" +frame.text());
//            sendAllMessage(frame.text());
//        }
//        super.channelRead(ctx, msg);
//    }


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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("客户端发送给服务器数据：" + msg.text());
        ctx.writeAndFlush(new TextWebSocketFrame("hello word!"));
        ctx.fireChannelActive();
    }
}