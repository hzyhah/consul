package com.cs.im.server;

import com.cs.im.handle.ChatSendHandle;
import com.cs.im.handle.LoginRequestHandle;
import com.cs.im.handle.ProcotoFrameDecoder;
import com.cs.im.message.MesssageCodecSharable;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpcxServer {

    public void socketServerStart(int port) throws InterruptedException {
        this.socketInit(port,1,1);
    }

    public void socketServerStart(int port, int worker) throws InterruptedException {
        this.socketInit(port,1,worker);
    }

    public void socketServerStart(int port, int bossNum, int worker) throws InterruptedException {
        this.socketInit(port, bossNum, worker);
    }

    public void socketInit(int port, int bossNum, int workerNum) throws InterruptedException {

        NioEventLoopGroup boss =  new NioEventLoopGroup(bossNum);
        NioEventLoopGroup worker = new NioEventLoopGroup(workerNum);

        // sharable handle
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        MesssageCodecSharable messsageCodecSharable = new MesssageCodecSharable();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProcotoFrameDecoder());
                            ch.pipeline().addLast(loggingHandler);
                            ch.pipeline().addLast(messsageCodecSharable);
                            ch.pipeline().addLast(new IdleStateHandler(5,0,0));
                            ch.pipeline().addLast(new ChannelDuplexHandler());
                            ch.pipeline().addLast(new LoginRequestHandle());
                            ch.pipeline().addLast(new ChatSendHandle());
                        }
                    });
            /*.childHandler(new WebSocketChannelInitializer());*/

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            /*if (channelFuture.isSuccess()){
                 serverSocketChannel = (ServerSocketChannel) channelFuture.channel();

            }*/
            ChannelFuture closeFuture = channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        UpcxServer server = new UpcxServer();
        try {
            server.socketServerStart(9091);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
