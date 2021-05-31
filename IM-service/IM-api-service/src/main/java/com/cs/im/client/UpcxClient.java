package com.cs.im.client;

import com.cs.im.client.locks.LoginLock;
import com.cs.im.handle.LoginAuthHandle;
import com.cs.im.handle.ProcotoFrameDecoder;
import com.cs.im.message.ChatSendMessage;
import com.cs.im.message.MesssageCodecSharable;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpcxClient {
    /**
     * upcx server ip or domain address
     */
    String address;
    /**
     * server port
     */
    int port;
    /**
     * 通讯管道，一个客户端对应一个通讯管道
     */
    private Channel channel =null;

    public static Long identityId;

    public UpcxClient(String address,int port){
        this.address =address;
        this.port =port;
    }

    private void initClient(String address,int port,LoginLock lock){
        NioEventLoopGroup group =  new NioEventLoopGroup();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MesssageCodecSharable messsageCodecSharable = new MesssageCodecSharable();

        try{
            Bootstrap bootstrap =  new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotoFrameDecoder());
                    ch.pipeline().addLast(loggingHandler);
                    ch.pipeline().addLast(messsageCodecSharable);
                    // 登陆请求
                    ch.pipeline().addLast("loginHandle", new LoginAuthHandle(lock));
                }
            });

            channel = bootstrap.connect(address,port).sync().channel();
            //发送登陆消息
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public Channel start() throws Exception {

        LoginLock loginLock  = new LoginLock(1);
        AtomicBoolean LOGIN_SUCCESS = new AtomicBoolean(false);
        initClient(address,port,loginLock);
        try {
            loginLock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (LOGIN_SUCCESS.get()){
            return channel;
        }else{
            throw new Exception("远程服务器拒绝连接，权限验证失败！");
        }
    }

    public static void main(String[] args) {
        UpcxClient client = new UpcxClient("127.0.0.1",9091);
        Channel channel = null;
        try {
            channel = client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //发送一对一聊天
       /* ChatSendMessage chatSendMessage = new ChatSendMessage(1,"你好！");
        client.channel.writeAndFlush(chatSendMessage);*/



        System.out.println("channel -----"+channel);
    }
}
