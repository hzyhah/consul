package com.cs.im.handle;

import com.cs.im.client.UpcxClient;
import com.cs.im.client.locks.LoginLock;
import com.cs.im.message.LoginRequestMessage;
import com.cs.im.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class LoginAuthHandle extends SimpleChannelInboundHandler<LoginResponseMessage> {

    LoginLock lock = null;

    public LoginAuthHandle(LoginLock lock) {
        this.lock = lock;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送登陆
        LoginRequestMessage  loginRequestMessage  = new LoginRequestMessage();
        loginRequestMessage.setName("admin");
        loginRequestMessage.setPassword("123");
        ctx.writeAndFlush(loginRequestMessage);
        super.channelActive(ctx);
        log.debug("请求远程服务器权限验证中...............");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponseMessage msg) throws Exception {
        //接受登陆响应
        log.debug("登陆响应处理");
        log.debug("登陆状态：{}",msg.getStatus());
        //登陆成功
        if (msg.getStatus()==1){
            lock.setLock(true);
            UpcxClient.identityId = msg.getIdentityId();
            //登陆失败
        }else{
            ctx.channel().close();
            log.debug("登陆验证失败，退出！！！status={}",msg.getStatus());
        }

        super.channelActive(ctx);
    }
}
