package com.nettyUp.bootstrap;

import com.nettyUp.exception.NettyUpException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.util.Assert;

public class NettyUpNode extends ServerBootstrap {

    public String ip;
    public int port;

    public NettyUpNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 注册是否成功
     */
    private boolean clusterEnable;

    /**
     * 获取本机通讯管道
     */
    private Channel channel;

    /**
     * 注册本机node
     * channelFuture 是sysn
     */
    public void registerNodes(ChannelFuture channelFuture) throws NettyUpException {
        Assert.notNull(channelFuture, "Error: You need Initialize param channelFuture!");
        if (channelFuture.isSuccess()){

        }else {
            throw new NettyUpException("channelFuture status is not success");
        }
    }

}
