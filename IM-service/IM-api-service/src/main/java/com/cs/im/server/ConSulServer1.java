package com.cs.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class ConSulServer1 {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup= new DefaultEventLoopGroup();
        new ServerBootstrap().group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringDecoder());

                        //没有使用DefaultEventLoopGroup来处理事件
                        ch.pipeline().addLast("hander1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                System.out.println(msg);
                                //处理完交给下一个hander去处理
                                super.channelRead(ctx, msg);
                            }
                            //使用DefaultEventLoopGroup来处理事件
                        }).addLast(eventLoopGroup,"hander2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                                super.channelRead(ctx, msg);
                            }
                        });

                        ch.pipeline().addLast("hander3",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("激活");
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("断开");
                                //断开后可自动连接

                            }
                        });

                    }
                }).bind(9091);

    }


}
