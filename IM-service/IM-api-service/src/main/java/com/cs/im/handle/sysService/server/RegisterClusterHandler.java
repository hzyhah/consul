package com.cs.im.handle.sysService.server;

import com.cs.im.handle.sysService.ServerClusterSupport;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RegisterClusterHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ServerClusterSupport clusterSupport = new ServerClusterSupport();
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
}
