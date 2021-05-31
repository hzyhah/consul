package com.cs.im.server;

import com.cs.im.client.locks.LoginLock;
import com.cs.im.config.JedisConfig;

import com.cs.im.handle.WebSocketChannelInitializer;

import com.nettyUp.sequence.IdWorker;
import com.nettyUp.storage.RedisStorage;
import com.nettyUp.storage.table.NodeRegisterInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

@Slf4j
public class UpcxServer1 {

    @Autowired
    Jedis jedis;

    private NioServerChannel nioServerChannel;
    private String registerId;
    LoginLock loginLock = new LoginLock(1);

    public NioServerChannel getChannel(){
        return nioServerChannel;
    }

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
//        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
//        MesssageCodecSharable messsageCodecSharable = new MesssageCodecSharable();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new WebSocketChannelInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()){
                NioServerChannel nioServerChannel = new NioServerChannel();
                nioServerChannel.init(channelFuture.channel());
                this.nioServerChannel = nioServerChannel;
                loginLock.countDown();
                //注册服务
                RedisStorage redisStorage = new RedisStorage(new JedisConfig().getJedis());
                NodeRegisterInfo nodeRegisterInfo  = new NodeRegisterInfo();
                nodeRegisterInfo.setNodeId(new IdWorker().nextId()+"");
                nodeRegisterInfo.setPort(port);
                nodeRegisterInfo.setIp("127.0.0.1");
                this.registerId = redisStorage.nodeRegister(nodeRegisterInfo);
            }else{
                nioServerChannel = null;
            }
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    //处理关闭的事件
                    if (registerId!=null && registerId.length()>0){

                    }
                }

            });
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


    public void send(String msg) throws InterruptedException {
        loginLock.await();
        Channel channel = this.nioServerChannel.getChannel();
        if (channel!=null){ channel.writeAndFlush(msg); }
    }

    public void close(){

    }

    public static void main(String[] args) throws InterruptedException {
        UpcxServer1 server = new UpcxServer1();

        try {
            server.socketServerStart(9091);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
