package com.cs.im.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Scanner;

public class ConSulClient {

    private static volatile Channel client = null;

    public static Channel getCilent(String ipAddress,int port) throws InterruptedException {
        if (client == null) {
            synchronized (ConSulClient.class) {
                if (client == null) {
                    client = new ConSulClient().initClient(ipAddress, port);
                }
            }
        }
        return client;
    }

    private Channel initClient(String ipAddress,int port) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        //启动类
            Bootstrap bootstrap =  new Bootstrap()
                //指定eventLoop
                .group(eventLoopGroup)
                // 选择客户端channel实现方式
                .channel(NioSocketChannel.class)
                //初始化处理器 连接建立后处理
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 再建立连接后，调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //添加编码器
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    }


                });


            //是同步调用
        ChannelFuture channelFuture=bootstrap.connect(ipAddress,port).sync(); //连接服务器地址

        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                eventLoopGroup.shutdownGracefully();
            }
        });

        return channelFuture.channel();
        /*channelFuture.channel().writeAndFlush("hello word!");//发送消息*/


        /*//  第二种方法调用
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.channel().writeAndFlush("hello word!");
            }
        });
        return null;*/


    }

    public static void main(String[] args) {
        Channel channel = null;
        try {
            channel = ConSulClient.getCilent("localhost",9091);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        Channel finalChannel = channel;
        Thread thread = new Thread(() -> {
            while(true){
                String line = scanner.nextLine();
                if ("quit".equalsIgnoreCase(line)){
                    finalChannel.close();
                    break;
                }
                finalChannel.writeAndFlush(line);
            }
        },"client");
        thread.start();
        /*//等客户端推出后 再执行下面代码
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        ChannelFuture closeFuture = channel.closeFuture();
        /*// 第一种关闭后
        try {
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //关闭之后要处理的操作
        System.out.println("客户端退出！！");*/

        //第二种关闭后操作处理 异步非阻塞触发
        /*closeFuture.addListener((ChannelFutureListener) future -> {
            System.out.println("客户端退出！！");
            //优雅的结束

        });*/

    }

}
