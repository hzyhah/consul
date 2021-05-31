package com.cs.im.server;

import io.netty.channel.Channel;


public class NioServerChannel {

    private String name ="";
    private Channel channel;

    public void init(Channel channel){
        this.name = "nioServer";
        this.channel = channel;
    }

    public Channel getChannel(){
        return channel;
    }

}
