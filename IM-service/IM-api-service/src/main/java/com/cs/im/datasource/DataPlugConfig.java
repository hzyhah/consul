package com.cs.im.datasource;


import com.cs.im.datasource.store.factory.IDataPlug;
import com.cs.im.datasource.store.factory.MomeryFactory;
import com.cs.im.session.Session;
import io.netty.channel.Channel;

public class DataPlugConfig {

    /*//是否启用外部数据源
    @Value("{upcx.server.datasource.plug}")*/
    private static String plug="momery";

    public IDataPlug getDataPlug(){
        if (plug!=null && !plug.equals("")){
            if (plug.equalsIgnoreCase("redis")){
                return null;
            }else{
                return MomeryFactory.getInstance();
            }
        }else{
            return null;
        }
    }

}
