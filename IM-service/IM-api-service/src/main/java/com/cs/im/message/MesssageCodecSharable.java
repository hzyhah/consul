package com.cs.im.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class MesssageCodecSharable extends MessageToMessageCodec<ByteBuf,Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        //魔数 5个字节
        out.writeBytes(Message.MAGIC_VAL.getBytes());
        //版本 1字节
        out.writeByte(1);
        //序列化方式  1个字节
        out.writeByte(1);
        //指令类型  1个字节
        out.writeByte(msg.getOrderType());
        //请求序号 4个字节
        out.writeInt(msg.getSequenceId());

        /*//对齐填充用
        out.writeByte(0xff);*/
        System.out.println("@@@@@@@@@@@@@");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(msg));
        byte[] bytes = mapper.writeValueAsString(msg).getBytes();
        // 正文长度  4个字节长度
        out.writeInt(bytes.length);
        //请求正文
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf magicVal = in.readBytes(5);
        ByteBuf version = in.readBytes(1);
        ByteBuf ser = in.readBytes(1);
        ByteBuf orderType = in.readBytes(1);
        ByteBuf sequenceId= in.readBytes(4);
        int length = in.readInt();
        System.out.println("lengh====="+length);
        log.debug("{},{},{},{},{}",magicVal,version,ser,orderType,sequenceId);
        byte[] bytes = new byte[length];
        in.readBytes(bytes,0,length);
        System.out.println("======================");
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(bytes, Message.class);
        out.add(message);
    }
}
