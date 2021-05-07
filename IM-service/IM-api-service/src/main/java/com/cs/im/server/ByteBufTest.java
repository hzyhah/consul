package com.cs.im.server;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class ByteBufTest {
    public static void main(String[] args) {
        //io.netty.allocator.type = [unpooled|pooled]
        //创建池化直接内存buffer
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(byteBuf.getClass());
        //创建池化堆内存buffer
        byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println(byteBuf.getClass());


    }
}
