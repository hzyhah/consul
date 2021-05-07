package com.cs.im.datasource.store.factory;

import com.cs.im.session.Session;
import io.netty.channel.Channel;


public interface IDataPlug {

    public long save(Channel channel, Session session);

    public Session getSession(Channel channel);

    public void remove(Channel channel);

    public Session getSession(long id);

    public Channel getChannel(long id);

}
