package com.cs.im.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import sun.awt.EmbeddedFrame;

public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                new MesssageCodecSharable()
        );

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangan","123","haha");
        channel.writeOutbound(loginRequestMessage);

        //decode
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MesssageCodec().encode(null,loginRequestMessage,byteBuf);
        //入站
        channel.writeInbound(byteBuf);
    }
}
