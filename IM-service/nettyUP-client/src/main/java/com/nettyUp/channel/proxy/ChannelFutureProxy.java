package com.nettyUp.channel.proxy;

import io.netty.channel.ChannelFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ChannelFutureProxy implements InvocationHandler {

    private ChannelFuture ChannelFuture;

    public ChannelFutureProxy(io.netty.channel.ChannelFuture ChannelFuture) {
        this.ChannelFuture = ChannelFuture;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------------start-------------");
        Object invoke = method.invoke(ChannelFuture, args);
        System.out.println("--------------end-------------");
        return invoke;
    }
}
