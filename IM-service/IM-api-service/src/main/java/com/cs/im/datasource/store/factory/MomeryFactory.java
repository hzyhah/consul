package com.cs.im.datasource.store.factory;

import com.cs.im.session.Session;
import io.netty.channel.Channel;

import java.util.HashMap;

public class MomeryFactory implements IDataPlug {

    private static MomeryFactory momeryFactory = new MomeryFactory();


    private static HashMap<Channel, Session> channelUpcxSessionHashMap = new HashMap<>();
    private static HashMap<Session, Channel> upcxSessionChannelHashMap = new HashMap<>();
    private static HashMap<Long, Session> sessionIdChannelHashMap = new HashMap<>();


    public static IDataPlug getInstance() {
        return momeryFactory;
    }

    @Override
    public long save(Channel channel, Session session) {
        channelUpcxSessionHashMap.put(channel, session);
        upcxSessionChannelHashMap.put(session, channel);
        sessionIdChannelHashMap.put(session.getSessionId(), session);
        return session.getSessionId();
    }

    @Override
    public Session getSession(Channel channel) {
        return channelUpcxSessionHashMap.get(channel);
    }

    @Override
    public void remove(Channel channel) {
        upcxSessionChannelHashMap.remove(channelUpcxSessionHashMap.get(channel));
        channelUpcxSessionHashMap.remove(channel);
    }

    @Override
    public Session getSession(long id) {
        return sessionIdChannelHashMap.get(id);
    }

    @Override
    public Channel getChannel(long id) {
        return upcxSessionChannelHashMap.get(sessionIdChannelHashMap.get(id));
    }
}
