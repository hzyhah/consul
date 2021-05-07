package com.cs.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class ConSulServer {


    public void initServer(){
        EventLoopGroup executors = new DefaultEventLoop();

        // 1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                // 2。指定selector和对应处理线程。用于监听accpet
                // 第一个NioEventLoopGroup 相当于boss负责accept事件，第二个相当于worker处理读写事件
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                // 3 选择服务serverSocketChannel实现
                .channel(NioServerSocketChannel.class)
                // 4 负责处理worker，指定worker处理任务类型
                .childHandler(
                        // 5 初始化channel，负责添加一些hander
                        new ChannelInitializer<NioSocketChannel>() {
                            //6 添加具体hander,连接后处理

                            @Override
                            protected void initChannel(NioSocketChannel sc) throws Exception {
                                //对传输消息解码
                                sc.pipeline().addLast(new StringDecoder());
                                //自定义hander
                                sc.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    // 重写read事件
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                            }
                        }
                ).bind(9090);//7 绑定端口
    }

    public static void main(String[] args) {
        ConSulServer conSulServer = new ConSulServer();
        conSulServer.initServer();

    }

}
