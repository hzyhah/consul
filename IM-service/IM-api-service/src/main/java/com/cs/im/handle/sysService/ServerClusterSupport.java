package com.cs.im.handle.sysService;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;

public class ServerClusterSupport implements ConfigTable {

    /**
     * 远程注册中心
     * @return
     */
    @Override
    public boolean Register() {

        return false;
    }

}
