package com.cs.im.session;

import com.cs.im.datasource.DataPlugConfig;
import com.cs.im.datasource.store.factory.IDataPlug;
import com.cs.im.datasource.store.factory.IdCreatePlus.IdWorker;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Configuration;

import java.util.Date;


@Configuration
public class SesssionFactory {

    private static SesssionFactory sesssionFactory = new SesssionFactory();

    public SesssionFactory getInstance(){
        return sesssionFactory;
    }

    private  static IDataPlug getDatePlug(){
        DataPlugConfig dataPlugConfig = new DataPlugConfig();
        return dataPlugConfig.getDataPlug();
    }

    public static Session register(Channel channel){
        UpcxSession upcxSession = new UpcxSession();
        upcxSession.sessionId= new IdWorker().nextId();
        upcxSession.creatTime = new Date();
        sesssionFactory.getDatePlug().save(channel,upcxSession);
        return upcxSession;
    }

    public void unRegister(Channel channel){
        sesssionFactory.getDatePlug().remove(channel);
    }


}
