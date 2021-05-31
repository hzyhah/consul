package com.nettyUP.storage;

import com.nettyUP.sequence.IdWorker;
import com.nettyUP.storage.table.ChannelUserInfo;
import com.nettyUP.storage.table.NodeChannelsInfo;
import com.nettyUP.storage.table.NodeRegisterInfo;

public interface StorageTools {
    public static final String NODE_REGISTER_STR="nettyUP@nodes";
    public static final String CHANNEL_USERS_REGISTER_STR="nettyUP@Channel_";
    public static final String USER_REGISTER_STR="nettyUP@User_";

    public String nodeRegister(NodeRegisterInfo nodeRegisterInfo);

    public void nodeUnRegister(String... vals);

    public void addUser(String key, NodeChannelsInfo nodeChannelsInfo);

    public void bindUserChannel(String key, ChannelUserInfo channelUserInfo);


    public default String getNodeId(){
        return new IdWorker().nextId()+"";
    }

    public default String getNodeRegisterKey(){
        return NODE_REGISTER_STR;
    }

    public default String getUserRegisterKeyStr(){
        return USER_REGISTER_STR;
    }

    public default String getChannelusersRegisterKeyStr(){
        return CHANNEL_USERS_REGISTER_STR;
    }
}
