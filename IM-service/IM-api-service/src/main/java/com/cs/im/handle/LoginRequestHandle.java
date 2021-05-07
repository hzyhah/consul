package com.cs.im.handle;

import com.cs.im.message.LoginRequestMessage;
import com.cs.im.message.LoginResponseMessage;
import com.cs.im.session.SesssionFactory;
import com.cs.im.session.UpcxSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginRequestHandle extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        log.debug("服务器端接收到登陆请求-- 用户名：{}----密码：{}　"+msg.getName(),msg.getPassword());

        //session 管理
        //sequenceId 消息序列号生成
        LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
        loginResponseMessage.setStatus(1);
        //建立会话
        UpcxSession upcxSession = (UpcxSession) SesssionFactory.register(ctx.channel());
        loginResponseMessage.setIdentityId(upcxSession.getSessionId());

        ctx.writeAndFlush(loginResponseMessage);


    }
}
