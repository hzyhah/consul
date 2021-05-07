package com.cs.im.handle;

import com.cs.im.message.ChatSendMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatSendHandle extends SimpleChannelInboundHandler<ChatSendMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatSendMessage msg) throws Exception {
        //获取一对一聊天倾听者
        long listener = msg.getListener();
        log.debug("已接收到一对一聊天信息，倾听者是："+listener);

        // 进行转发

    }
}
