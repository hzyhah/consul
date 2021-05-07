package com.cs.im.session;


import io.netty.channel.Channel;

import java.util.Date;

/**
 * 会话接口
 */
public interface Session {

    long sessionId = 0;
    public Date creatTime =null;

    public long getSessionId();

    public Date getCreatTime();

    public Session getSession(Channel channel);

    public Session getSession(long sessionId);

    Object getAttribube(Object key);

    void setAttribube(Object key,Object value);



}
