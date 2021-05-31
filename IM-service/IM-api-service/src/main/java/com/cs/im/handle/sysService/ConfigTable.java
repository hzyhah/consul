package com.cs.im.handle.sysService;

import io.netty.channel.ChannelHandlerContext;

import java.nio.channels.Channel;

public interface ConfigTable {
    public static final String NODES_KEY = "nettyNodes_";

    public boolean Register();
}
