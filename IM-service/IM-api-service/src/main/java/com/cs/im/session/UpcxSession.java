package com.cs.im.session;

import com.cs.im.datasource.DataPlugConfig;
import com.cs.im.datasource.store.factory.IDataPlug;
import com.cs.im.datasource.store.factory.IdCreatePlus.IdWorker;
import io.netty.channel.Channel;

import java.util.Date;
import java.util.HashMap;

public class UpcxSession implements Session {

    private static HashMap map = new HashMap();

    long sessionId;
    Date creatTime;

    public long getSessionId() {
        return sessionId;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    private  static IDataPlug getDatePlug(){
        DataPlugConfig dataPlugConfig = new DataPlugConfig();
        return dataPlugConfig.getDataPlug();
    }

    @Override
    public Session getSession(Channel channel) {
        return getDatePlug().getSession(channel);
    }

    @Override
    public Session getSession(long sessionId) {
        return getDatePlug().getSession(sessionId);
    }

    @Override
    public Object getAttribube(Object key) {
        return map.get(key);
    }

    @Override
    public void setAttribube(Object key, Object value) {
        map.put(key,value);
    }
}
